//
// Created by Sidhant Srikumar on 8/14/17.
//

#include <jni.h>

JNIEXPORT jstring JNICALL
Java_co_sidhant_smarterplaylists_spotify_SpotifyAuthHelper_getClientSecret(JNIEnv *env, jobject instance) {

 return (*env)->  NewStringUTF(env, "DUMMYSECRET");
}