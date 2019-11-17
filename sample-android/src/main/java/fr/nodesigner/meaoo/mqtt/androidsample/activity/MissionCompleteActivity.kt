package fr.nodesigner.meaoo.mqtt.androidsample.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import fr.nodesigner.meaoo.androidsample.R
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Mission
import kotlinx.android.synthetic.main.mission_activity.buttonGo
import kotlinx.android.synthetic.main.mission_activity.llTargets
import kotlinx.android.synthetic.main.mission_activity.tvMission
import kotlinx.android.synthetic.main.target_view_item.view.button

class MissionCompleteActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mission_complete_activity)
    }
}
