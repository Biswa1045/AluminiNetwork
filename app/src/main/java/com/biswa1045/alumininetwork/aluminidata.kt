package com.biswa1045.alumininetwork


class aluminidata {
    var NAME: String? = null
    var IMAGE: String? = null
    var BATCH: String? = null
    var BRANCH: String? = null
    var ID: String? = null
    fun aluminidata() {}
    fun aluminidata(
        NAME: String?, BATCH: String?, BRANCH: String?, IMAGE: String?,ID:String?){
        this.NAME = NAME
        this.BATCH = BATCH
        this.BRANCH = BRANCH
        this.IMAGE = IMAGE
        this.ID = ID
    }

}
