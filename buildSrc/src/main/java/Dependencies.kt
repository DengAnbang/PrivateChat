object Versions {

    val support_version = "1.2.1"
    val retrofit_version = "2.3.0"
    val rxjava = "2.1.9"
}

object Libs {
    //build
    val appcompat = "androidx.appcompat:appcompat:1.2.0"
    val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.2"


    //test
    val junit = "junit:junit:4.12"
    val runner = "androidx.test:runner:1.1.1"
    val espresso_core = "androidx.test.espresso:espresso-core:3.3.0"
    val multidex = "com.android.support:multidex:1.0.3"
    val crashreport_upgrade = "com.tencent.bugly:crashreport_upgrade:1.4.1"



    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit_version}"
    val retrofit_rxjava_adapter =
        "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit_version}"
    val adapter_rxjava2 = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit_version}"
    val converter_gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit_version}"
    val rxjava = "io.reactivex.rxjava2:rxjava:${Versions.rxjava}"

    //support
    val recyclerview = "androidx.recyclerview:recyclerview:${Versions.support_version}"
    val material = "com.google.android.material:material:${Versions.support_version}"
    val swiperefreshlayout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.support_version}"
    val rxandroid = "io.reactivex.rxjava2:rxandroid:2.0.1"
    val rxbinding = "com.jakewharton.rxbinding2:rxbinding:2.0.0"
    val rxpermissions = "com.tbruyelle.rxpermissions2:rxpermissions:0.9.4@aar"

    //进程保活
    val hellodaemon = "com.xdandroid:hellodaemon:+"

    //EasySocket    https://github.com/jiusetian/EasySocket
    val easySocket = "com.github.jiusetian:EasySocket:v2.1.1"

    //语音 2f28a4e830fb84d0da705096
    val JuphoonCloud = "com.JuphoonCloud:JC-SDK:2.5"
    val PictureSelector = "com.github.LuckSiege.PictureSelector:picture_library:v2.6.1"
}