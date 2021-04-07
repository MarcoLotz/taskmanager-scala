package com.marcolotz.taskmanager.model

sealed trait Priority { def priorityNumber: Int }

case object LOW_PRIORITY extends Priority { val priorityNumber = 0 }
case object MEDIUM_PRIORITY extends Priority { val priorityNumber = 500 }
case object HIGH_PRIORITY extends Priority { val priorityNumber = 1000 }