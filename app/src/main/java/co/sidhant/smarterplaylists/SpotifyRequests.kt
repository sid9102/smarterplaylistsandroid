package co.sidhant.smarterplaylists

import com.beust.klaxon.*
import khttp.get
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.Map

/**
 * Created by sid on 7/24/17.
 * Helper for Spotify API requests
 */
class SpotifyRequests (authToken: String, clientID: String): AnkoLogger
{
    var mAuthToken : String = authToken
    var mClientID : String = clientID
    companion object
    {
        const val BASE_URL: String = "https://api.spotify.com/"
    }

    data class SpotifyEntity(val name: String, val uri: String)

    fun parse(body: String) : Any? {
        val parser: Parser = Parser()
        val stringBuilder: StringBuilder = StringBuilder(body)
        return parser.parse(stringBuilder) as JsonObject
    }

    fun getPlaylists(playlists: ArrayList<SpotifyEntity>)
    {
        // TODO: get all playlists
        val r = get(BASE_URL + "v1/me/playlists", headers = mapOf("Authorization" to "Bearer " + mAuthToken))
        val playlistJson = parse(r.text) as JsonObject
        playlistJson.array<JsonObject>("items")!!.mapTo(playlists)
        {
            SpotifyEntity(it.string("name") as String, it.string("id") as String)
        }
    }
}