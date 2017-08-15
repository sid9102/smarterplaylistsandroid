package co.sidhant.smarterplaylists

import android.content.SharedPreferences

/**
 * For managing global preferences
 * Created by sid on 8/15/17.
 */
object PrefManager
{
    private val REFRESH_TOKEN_KEY = "refreshToken"
    private val COUNTRY_KEY = "country"
    lateinit var sharedPrefs : SharedPreferences

    var refreshToken: String
        get()
        {
            return sharedPrefs.getString(REFRESH_TOKEN_KEY, null)
        }
        set(refreshToken)
        {
            val editor = sharedPrefs.edit()
            editor.putString(REFRESH_TOKEN_KEY, refreshToken)
            editor.apply()
        }

    var userCountry: String
        get()
        {
            return sharedPrefs.getString(COUNTRY_KEY, null)
        }
        set(countryCode)
        {
            val editor = sharedPrefs.edit()
            editor.putString(COUNTRY_KEY, countryCode)
            editor.apply()
        }

    fun isFirstRun() : Boolean
    {
        return sharedPrefs.getString(REFRESH_TOKEN_KEY, null) == null
    }
}