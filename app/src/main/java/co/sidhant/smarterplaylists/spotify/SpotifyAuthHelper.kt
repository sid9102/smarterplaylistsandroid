package co.sidhant.smarterplaylists.spotify

import khttp.post as httpPost

/**
 * A helper for Spotify authentication
 * Created by sid on 8/14/17.
 */
object SpotifyAuthHelper
{
    init
    {
        System.loadLibrary("hello-jni")
    }

    external fun getClientSecret() : String

    var refreshToken = ""

    fun getAccessTokenFromCode(code: String) : String
    {
        val r = httpPost("https://accounts.spotify.com/api/token")
        return ""
    }

    fun getNewAccessToken(refreshToken: String)
    {

    }
}