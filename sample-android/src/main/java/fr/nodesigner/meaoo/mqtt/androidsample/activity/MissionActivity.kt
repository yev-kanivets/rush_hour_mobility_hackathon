package fr.nodesigner.meaoo.mqtt.androidsample.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import fr.nodesigner.meaoo.androidsample.R
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Mission
import kotlinx.android.synthetic.main.mission_activity.buttonGo
import kotlinx.android.synthetic.main.mission_activity.llTargets
import kotlinx.android.synthetic.main.mission_activity.tvMission
import kotlinx.android.synthetic.main.target_view_item.view.button

class MissionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mission_activity)

        val mission = intent.getParcelableExtra<Mission>(KEY_MISSION)

        tvMission.text = mission.mission
        Log.v("test", mission.positions.toString())
        mission.positions.forEach { target ->
            val targetView = layoutInflater.inflate(R.layout.target_view_item, null)
            targetView.button.text = "x: ${target.x}, y: ${target.y}"
            llTargets.addView(targetView)
        }

        buttonGo.setOnClickListener { setResult(RESULT_OK); finish() }
    }

    companion object {

        private const val KEY_MISSION = "key_mission"

        fun newIntent(
                context: Context,
                mission: Mission
        ) = Intent(context, MissionActivity::class.java).apply {
            putExtra(KEY_MISSION, mission)
        }
    }
}
