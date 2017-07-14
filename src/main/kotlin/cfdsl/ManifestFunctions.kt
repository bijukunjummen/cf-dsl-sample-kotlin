package cfdsl


fun cf(init: CfManifest.() -> Unit) = CfManifest().apply(init)

//fun cf(init: CfManifest.() -> Unit): CfManifest {
//    val manifest = CfManifest()
//    manifest.init()
//    return manifest
//}