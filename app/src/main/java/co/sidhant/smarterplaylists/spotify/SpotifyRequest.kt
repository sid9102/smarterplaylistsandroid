package co.sidhant.smarterplaylists.spotify

import android.util.Log
import co.sidhant.smarterplaylists.PrefManager
import com.beust.klaxon.*
import khttp.responses.Response
import khttp.get as httpGet
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by sid on 7/24/17.
 * Helper singleton for Spotify API requests
 */
object SpotifyRequest : AnkoLogger
{
    lateinit var accessToken: String
    const val BASE_URL: String = "https://api.spotify.com/"

    private fun getWithAuth(url: String, params: Map<String, String> = mapOf()) : Response
    {
        var r = httpGet(url, headers = mapOf("Authorization" to "Bearer $accessToken"), params = params)
        if(r.statusCode == 401)
        {
            // Access code needs to be refreshed
            accessToken = AuthHelper.getNewAccessToken()!!
            r = httpGet(url, headers = mapOf("Authorization" to "Bearer $accessToken"), params = params)
            Log.i("SpotifyRequest", "Got new accessToken")
        }
        return r
    }

    private fun parse(body: String) : JsonObject
    {
        val parser = Parser()
        val stringBuilder = StringBuilder(body)
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
            result = parse(r.text).string("name") as String
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
        val playlist = parse(r.text).obj("tracks")!!.array<JsonObject>("items")
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
        var newURL = parse(r.text).string("href") as String
        newURL = limitRegex.replace(newURL, "limit=50")
        for(i in 0..2)
        {
            val curOffset = i * 50
            newURL = offsetRegex.replace(newURL, "offset=" + curOffset.toString())
            info("Getting playlists from URL: " + newURL)
            r = getWithAuth(newURL)
            val playlistJson = parse(r.text)
            playlistJson.array<JsonObject>("items")!!.mapTo(playlists)
            {
                SpotifyEntity(it.string("name") as String, it.string("uri") as String)
            }
        }
    }

    fun getUserCountryCode() : String
    {
        val r = getWithAuth(BASE_URL + "v1/me")
        val response = parse(r.text)
        return response.string("country")!!
    }

    fun isUserPremium() : Boolean
    {
        val r = getWithAuth(BASE_URL + "v1/me")
        val response = parse(r.text)
        return response.string("product") == "premium"
    }


    fun getArtistTopTracks(artist: SpotifyEntity) : ArrayList<SpotifySong>
    {
        val result = ArrayList<SpotifySong>()
        val id = artist.uri
        val r = getWithAuth(BASE_URL + "v1/artists/{$id}/top-tracks", params = mapOf("country" to PrefManager.userCountry!!))
        val tracks = parse(r.text).array<JsonObject>("tracks")
        tracks!!.mapTo(result)
        {
            SpotifySong(it.obj("track")!!.string("name") as String,
                    it.obj("track")!!.string("uri") as String,
                    artist.name)
        }
        return result
    }
}