package co.sidhant.smarterplaylists.spotify

import co.sidhant.smarterplaylists.PrefManager
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.beust.klaxon.string
import khttp.post as httpPost

/**
 * A helper for Spotify authentication
 * Created by sid on 8/14/17.
 */
object AuthHelper
{
    val client_id = "ee7a464c2dc4410e972b78568ddde051"
    init
    {
        System.loadLibrary("keys")
    }

    private fun parse(body: String) : JsonObject
    {
        val parser = Parser()
        val stringBuilder = StringBuilder(body)
        return parser.parse(stringBuilder) as JsonObject
    }

    private external fun getClientSecret() : String

    fun getAccessTokenFromCode(code: String) : String
    {
        val payload = mapOf("code" to code, "client_id" to client_id,
                "client_secret" to getClientSecret(),
                "redirect_uri" to "sidhant://sidhant.co/spotify/",
                "grant_type" to "authorization_code")
        val url = "https://accounts.spotify.com/api/token"
        val r = httpPost(url, params = payload)
        val response = parse(r.text)
        val accessToken = response.string("access_token")!!
        val refreshToken = response.string("refresh_token")!!
        PrefManager.refreshToken = refreshToken

        return accessToken
    }

    fun getNewAccessToken() : String?
    {
        val refreshToken = PrefManager.refreshToken!!
        val payload = mapOf("refresh_token" to refreshToken,
                "client_id" to client_id,
                "client_secret" to getClientSecret(),
                "grant_type" to "refresh_token")
        val url = "https://accounts.spotify.com/api/token"
        val r = httpPost(url, params = payload)
        val response = parse(r.text)
        val accessToken = response.string("access_token")
        if(accessToken == null)
        {
            PrefManager.resetLogin()
        }
        return accessToken
    }
}