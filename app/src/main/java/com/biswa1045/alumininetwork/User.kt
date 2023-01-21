package com.biswa1045.alumininetwork

class User {
    var name:String? =null
    var details:String? =null
    var uid:String? =null
    var img:String? =null

    constructor(){

    }
    constructor(name:String?,details:String?,uid:String?,img:String?){
        this.name=name
        this.details=details
        this.uid=uid
        this.img=img
    }
}