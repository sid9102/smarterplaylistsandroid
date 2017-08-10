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
        private var playingEntity : SpotifyEntity? = null
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
            // Cancel any other delayed pause
            handler.removeCallbacksAndMessages(null)
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

        anim = ProgressBarAnimation(progress, 30000)
        progress.progress = 3000

        super.addView(playButton)
        super.addView(progress)
    }

    fun setProgressMax()
    {
        setIconPlay()
        setProgress(progress.max)
    }

    fun setProgress(progress: Int)
    {
        if (progress != this.progress.max)
            setIconStop()
        anim.setProgress(progress)
    }

    private fun togglePlaying()
    {
        if(icon == R.drawable.play)
            startPlaying()
        else
            stopPlaying()
    }

    private fun startPlaying()
    {
        playingButton?.stopPlaying()
        playingButton = this
        playingEntity = this.entity

        PlayerManager.play(entity)
        setProgress(0)
        handler.postDelayed(
                {
                    // Pause the currently playing track
                    this@PreviewButton.stopPlaying()
                }, 30000)
        entity.playing = true
        setIconStop()
    }

    private fun stopPlaying()
    {
        setProgressMax()
        setIconPlay()
        playingEntity?.playing = false
        playingEntity = null
        PlayerManager.stop()
    }

    private fun setIconPlay()
    {
        icon = R.drawable.play
        playButton.imageResource = icon
    }

    private fun setIconStop()
    {
        icon = R.drawable.stop
        playButton.imageResource = icon
    }

    /**
     * @param fullDuration - time required to fill progress from 0% to 100%
     */
    inner class ProgressBarAnimation (private val mProgressBar: ProgressBar, fullDuration: Long) : Animation()
    {
        private var mTo: Int = 0
        private var mFrom: Int = 0
        private val mStepDuration: Long = fullDuration / mProgressBar.max


        fun setProgress(progress: Int)
        {
            var curProgress = progress
            if (curProgress < 0)
            {
                curProgress = 0
            }

            if (curProgress > mProgressBar.max)
            {
                curProgress = mProgressBar.max
            }

            if(curProgress == mProgressBar.max)
            {
                mProgressBar.clearAnimation()
                mProgressBar.progress = curProgress
            }
            else
            {
                mTo = 0

                mFrom = mProgressBar.max - curProgress
                duration = Math.abs(mTo - mFrom) * mStepDuration
                mProgressBar.startAnimation(this)
            }
        }

        override fun applyTransformation(interpolatedTime: Float, t: Transformation)
        {
            val value = mFrom + (mTo - mFrom) * interpolatedTime
            mProgressBar.progress = value.toInt()
        }
    }

}