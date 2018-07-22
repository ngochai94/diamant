package com.dhai.entity

sealed trait UserStatus

case object Inactive extends UserStatus

case object Pending extends UserStatus

case object StopDecided extends UserStatus

case object ContinueDecided extends UserStatus

case class UserState(status: UserStatus, score: Int, pendingScore: Int)
