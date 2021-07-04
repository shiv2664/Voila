package com.android.testproject1.model

import android.net.Uri

// data class MultipleImage(val local_path: String="",val local_uri: Uri?,val url:String="") {
//}


data class MultipleImage(var local_path: String? = "" ) {}

//var url: String? = null,
//var local_uri: Uri? = null

// constructor() {}
// constructor(url: String?) {
//  this.url = url
// }

// constructor(local_path: String?, local_uri: Uri?) {
//  this.local_path = local_path
//  this.local_uri = local_uri
// }

