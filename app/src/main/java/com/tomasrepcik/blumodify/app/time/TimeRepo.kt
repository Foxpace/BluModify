package com.tomasrepcik.blumodify.app.time

import javax.inject.Inject

class TimeRepo @Inject constructor(): TimeRepoTemplate{

    override fun currentMillis(): Long = System.currentTimeMillis()

}