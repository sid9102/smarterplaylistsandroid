package co.sidhant.smarterplaylists

import com.beust.klaxon.*
import khttp.responses.Response
import khttp.get as httpGet
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by sid on 7/24/17.
 * Helper for Spotify API requests
 */
class SpotifyRequests (authToken: String): AnkoLogger
{
    var mAuthToken : String = authToken
    companion object
    {
        const val BASE_URL: String = "https://api.spotify.com/"
    }

    data class SpotifyEntity(val name: String, val uri: String)

    fun getWithAuth(url: String) : Response
    {
        return httpGet(url, headers = mapOf("Authorization" to "Bearer " + mAuthToken))
    }

    fun parse(body: String) : Any?
    {
        val parser: Parser = Parser()
        val stringBuilder: StringBuilder = StringBuilder(body)
        return parser.parse(stringBuilder) as JsonObject
    }

    fun getName(uri: String) : String
    {
        var result = ""
        if (uri.contains("playlist"))
        {
            val parts = uri.split(":")
            val url = BASE_URL + "v1/users/" + parts[2] + "/playlists/" + parts[4]
            val r = getWithAuth(url)
            result = (parse(r.text) as JsonObject).string("name") as String
        }
        else
        {
            // TODO: handle other types of URI
        }
        return result
    }

    fun getPlaylistTracks(uri: String) : ArrayList<SpotifyRequests.SpotifyEntity>
    {
        val result = ArrayList<SpotifyEntity>()
        val parts = uri.split(":")
        val url = BASE_URL + "v1/users/" + parts[2] + "/playlists/" + parts[4]
        val r = getWithAuth(url)
        val playlist = (parse(r.text) as JsonObject).obj("tracks")!!.array<JsonObject>("items")
        playlist!!.mapTo(result) { SpotifyEntity(it.obj("track")!!.string("name") as String, it.obj("track")!!.string("uri") as String) }
        return result
    }

    fun getPlaylistsForCurrentUser(playlists: ArrayList<SpotifyEntity>)
    {
        var r = getWithAuth(BASE_URL + "v1/me/playlists")
        // Spotify only gives us 20 playlists by default, we want to get the maximum 150
        val limitRegex = Regex("limit=\\d+")
        val offsetRegex = Regex("offset=\\d+")
        var newURL = (parse(r.text) as JsonObject).string("href") as String
        newURL = limitRegex.replace(newURL, "limit=50")
        for(i in 0..2)
        {
            val curOffset = i * 50
            newURL = offsetRegex.replace(newURL, "offset=" + curOffset.toString())
            info("Getting playlists from URL: " + newURL)
            r = getWithAuth(newURL)
            val playlistJson = parse(r.text) as JsonObject
            playlistJson.array<JsonObject>("items")!!.mapTo(playlists)
            {
                SpotifyEntity(it.string("name") as String, it.string("uri") as String)
            }
        }
    }
}