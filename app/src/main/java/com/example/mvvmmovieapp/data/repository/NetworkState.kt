package com.example.mvvmmovieapp.data.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED

}

class NetworkState(val status: Status, val msg: String) {

    companion object {
        @JvmField
        val LOADED: NetworkState
        @JvmField
        val LOADING: NetworkState
        @JvmField
        val ERROR: NetworkState
        @JvmField
        val ENDOFLIST: NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS, "Success")

            LOADING = NetworkState(Status.RUNNING, "Running")

            ERROR = NetworkState(Status.FAILED, "Something went wrong")

            ENDOFLIST = NetworkState(Status.FAILED, "You have reached the end")
        }
    }
}