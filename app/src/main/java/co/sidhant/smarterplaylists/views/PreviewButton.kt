package co.sidhant.smarterplaylists.views

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.RelativeLayout
import co.sidhant.smarterplaylists.PlayerManager
import co.sidhant.smarterplaylists.R
import co.sidhant.smarterplaylists.spotify.SpotifyEntity
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import android.view.animation.Transformation
import android.view.animation.Animation


/**
 *
 * Created by sid on 8/4/17.
 */
class PreviewButton(context: Context) : RelativeLayout(context)
{
    companion object
    {
        private var playingButton : PreviewButton? = null
    }

    lateinit var entity : SpotifyEntity
    private lateinit var playButton : ImageButton
    private lateinit var progress : ProgressBar
    private var icon = R.drawable.play
    private lateinit var anim : ProgressBarAnimation

    fun initView(entity: SpotifyEntity)
    {
        this.entity = entity
        playButton = ImageButton(context)
        playButton.imageResource = icon
        val buttonLayout = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        buttonLayout.addRule(RelativeLayout.CENTER_HORIZONTAL)
        buttonLayout.addRule(RelativeLayout.CENTER_VERTICAL)
        playButton.layoutParams = buttonLayout
        playButton.setBackgroundColor(Color.parseColor("#424242"))

        playButton.onClick()
        {
            togglePlaying()
        }

        progress = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
        progress.isIndeterminate = false
        val progressLayout = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        progressLayout.addRule(RelativeLayout.CENTER_HORIZONTAL)
        progressLayout.addRule(RelativeLayout.CENTER_VERTICAL)
        progress.layoutParams = progressLayout
        progress.max = 3000
        progress.progressDrawable = context.getDrawable(R.drawable.preview_progress)

        anim = ProgressBarAnimation(progress, 0f, 3000f)
        anim.duration = 30000
        progress.progress = 3000

        super.addView(playButton)
        super.addView(progress)
    }

    fun togglePlaying()
    {
        if(icon == R.drawable.play)
            startPlaying()
        else
            stopPlaying()
    }

    fun startPlaying()
    {
        playingButton?.stopPlaying()
        playingButton = this
        icon = R.drawable.stop
        anim.reset()
        progress.startAnimation(anim)
        PlayerManager.play(entity)
        Handler().postDelayed(
                {
                    // Pause the currently playing track if the same button is still playing
                    if (this@PreviewButton == playingButton)
                    {
                        playingButton?.stopPlaying()
                    }
                }, 30000)
        playButton.imageResource = icon
    }

    fun stopPlaying()
    {
        icon = R.drawable.play
        PlayerManager.stop()
        progress.clearAnimation()
        progress.progress = 3000
        playButton.imageResource = icon
        playingButton = null
    }

    inner class ProgressBarAnimation(private val progressBar: ProgressBar, private val from: Float, private val to: Float) : Animation()
    {

        override fun applyTransformation(interpolatedTime: Float, t: Transformation)
        {
            super.applyTransformation(interpolatedTime, t)
            val value = from + (to - from) * interpolatedTime
            progressBar.progress = value.toInt()
        }

    }

}