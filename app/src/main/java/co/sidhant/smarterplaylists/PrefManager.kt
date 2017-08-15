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
    private val PREMIUM_KEY = "isUserPremium"
    lateinit var sharedPrefs : SharedPreferences

    private var _refreshToken : String? = null
    var refreshToken: String?
        get()
        {
            if(_refreshToken == null)
                _refreshToken = sharedPrefs.getString(REFRESH_TOKEN_KEY, null)
            return _refreshToken
        }
        set(refreshToken)
        {
            _refreshToken = refreshToken
            val editor = sharedPrefs.edit()
            editor.putString(REFRESH_TOKEN_KEY, _refreshToken)
            editor.apply()
        }

    private var _userCountry : String? = null
    var userCountry: String?
        get()
        {
            if (_userCountry == null)
                _userCountry = sharedPrefs.getString(COUNTRY_KEY, null)
            return _userCountry
        }
        set(countryCode)
        {
            _userCountry = countryCode
            val editor = sharedPrefs.edit()
            editor.putString(COUNTRY_KEY, _userCountry)
            editor.apply()
        }

    private var _isUserPremium : Boolean? = null
    var isUserPremium: Boolean
        get()
        {
            if(_isUserPremium == null)
                _isUserPremium = sharedPrefs.getBoolean(PREMIUM_KEY, false)
            return _isUserPremium!!
        }
        set(premium)
        {
            _isUserPremium = premium
            val editor = sharedPrefs.edit()
            editor.putBoolean(PREMIUM_KEY, _isUserPremium!!)
            editor.apply()
        }

    fun isFirstRun() : Boolean
    {
        val result = this.refreshToken == null || this.userCountry == null
        return result
    }

    // In case AuthHelper is unable to get a valid access token
    fun resetLogin()
    {
        sharedPrefs.edit().remove(REFRESH_TOKEN_KEY).apply()
        this._refreshToken = null
    }
}