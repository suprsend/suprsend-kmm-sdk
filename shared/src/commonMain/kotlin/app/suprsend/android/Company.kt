package app.suprsend.android

import  kotlinx.serialization.Serializable

@Serializable
data class Company(val id: String, val email: String)