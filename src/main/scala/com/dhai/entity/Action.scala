package com.dhai.entity

case class Action(user: String, choice: UserChoice)

sealed trait UserChoice
case object Stop extends UserChoice
case object Continue extends UserChoice
