package com.scw.twtour.util

sealed class PermissionState

object AccessFineLocationNone: PermissionState()
object AccessFineLocationGranted: PermissionState()
object AccessFineLocationDenied: PermissionState()
