package cfdsl


fun cf(init: CfManifest.() -> Unit) = CfManifest().apply(init)