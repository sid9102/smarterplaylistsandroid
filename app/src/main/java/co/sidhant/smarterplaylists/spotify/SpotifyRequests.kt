package co.sidhant.smarterplaylists.spotify

import com.beust.klaxon.*
import khttp.responses.Response
import khttp.get as httpGet
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by sid on 7/24/17.
 * Helper singleton for Spotify API requests
 */
object SpotifyRequests: AnkoLogger
{
    var accessToken: String = ""
        set(value)
        {
            field = value
        }
    const val BASE_URL: String = "https://api.spotify.com/"

    fun getWithAuth(url: String) : Response
    {
        return httpGet(url, headers = mapOf("Authorization" to "Bearer " + accessToken))
    }

    private fun parse(body: String) : Any?
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

    fun getPlaylistTracks(uri: String) : ArrayList<SpotifySong>
    {
        fun getArtistString(artists: JsonArray<JsonObject>) : String
        {
            if (artists.size == 1)
                return artists[0].string("name") as String
            var result = ""
            for (artist in artists)
            {
                result += artist.string("name") + ", "
            }
            return result.substring(0, result.length - 2)
        }

        val result = ArrayList<SpotifySong>()
        val parts = uri.split(":")
        val url = BASE_URL + "v1/users/" + parts[2] + "/playlists/" + parts[4]
        val r = getWithAuth(url)
        val playlist = (parse(r.text) as JsonObject).obj("tracks")!!.array<JsonObject>("items")
        playlist!!.mapTo(result)
        {
            SpotifySong(it.obj("track")!!.string("name") as String,
                    it.obj("track")!!.string("uri") as String,
                    getArtistString(it.obj("track")!!.array<JsonObject>("artists") as JsonArray<JsonObject>))
        }
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

    fun getUserCountryCode() : String
    {
        val r = getWithAuth(BASE_URL + "v1/me")
        val response = parse(r.text) as JsonObject
        return response["country"] as String
    }


    fun getArtistTopTracks(artist: SpotifyEntity, countryCode: String) : ArrayList<SpotifySong>
    {
        // TODO: finish this
        val tracks = ArrayList<SpotifySong>()
        val id = artist.uri
        var r = getWithAuth(BASE_URL + "v1/artists/{$id}/top-tracks")
        return tracks
    }
}