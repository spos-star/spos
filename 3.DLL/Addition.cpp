#include "Addition.h"

JNIEXPORT int JNICALL Java_Addition_add(JNIEnv *env,jobject javaobj,jint n1,jint n2){
    return n1+n2;
}