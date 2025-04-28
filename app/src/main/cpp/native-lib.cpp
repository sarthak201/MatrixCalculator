#include <jni.h>
#include <vector>

extern "C"
JNIEXPORT jdoubleArray JNICALL
Java_com_example_matrixcalculatorapp_MainActivity_addMatrices(JNIEnv *env, jobject thiz, jdoubleArray matrix_a, jdoubleArray matrix_b, jint rows, jint cols) {
    jsize size = rows * cols;
    std::vector<double> a(size);
    std::vector<double> b(size);

    env->GetDoubleArrayRegion(matrix_a, 0, size, a.data());
    env->GetDoubleArrayRegion(matrix_b, 0, size, b.data());

    std::vector<double> result(size);
    for (int i = 0; i < size; i++) {
        result[i] = a[i] + b[i];
    }

    jdoubleArray resultArray = env->NewDoubleArray(size);
    env->SetDoubleArrayRegion(resultArray, 0, size, result.data());
    return resultArray;
}

extern "C"
JNIEXPORT jdoubleArray JNICALL
Java_com_example_matrixcalculatorapp_MainActivity_subtractMatrices(JNIEnv *env, jobject thiz, jdoubleArray matrix_a, jdoubleArray matrix_b, jint rows, jint cols) {
    jsize size = rows * cols;
    std::vector<double> a(size);
    std::vector<double> b(size);

    env->GetDoubleArrayRegion(matrix_a, 0, size, a.data());
    env->GetDoubleArrayRegion(matrix_b, 0, size, b.data());

    std::vector<double> result(size);
    for (int i = 0; i < size; i++) {
        result[i] = a[i] - b[i];
    }

    jdoubleArray resultArray = env->NewDoubleArray(size);
    env->SetDoubleArrayRegion(resultArray, 0, size, result.data());
    return resultArray;
}

extern "C"
JNIEXPORT jdoubleArray JNICALL
Java_com_example_matrixcalculatorapp_MainActivity_multiplyMatrices(JNIEnv *env, jobject thiz, jdoubleArray matrix_a, jdoubleArray matrix_b, jint rows_a, jint cols_a, jint rows_b, jint cols_b) {
    std::vector<double> a(rows_a * cols_a);
    std::vector<double> b(rows_b * cols_b);

    env->GetDoubleArrayRegion(matrix_a, 0, rows_a * cols_a, a.data());
    env->GetDoubleArrayRegion(matrix_b, 0, rows_b * cols_b, b.data());

    std::vector<double> result(rows_a * cols_b);
    for (int i = 0; i < rows_a; i++) {
        for (int j = 0; j < cols_b; j++) {
            double sum = 0.0;
            for (int k = 0; k < cols_a; k++) {
                sum += a[i * cols_a + k] * b[k * cols_b + j];
            }
            result[i * cols_b + j] = sum;
        }
    }

    jdoubleArray resultArray = env->NewDoubleArray(rows_a * cols_b);
    env->SetDoubleArrayRegion(resultArray, 0, rows_a * cols_b, result.data());
    return resultArray;
}

extern "C"
JNIEXPORT jdoubleArray JNICALL
Java_com_example_matrixcalculatorapp_MainActivity_divideMatrices(JNIEnv *env, jobject thiz, jdoubleArray matrix_a, jdoubleArray matrix_b, jint rows, jint cols) {
    jsize size = rows * cols;
    std::vector<double> a(size);
    std::vector<double> b(size);

    env->GetDoubleArrayRegion(matrix_a, 0, size, a.data());
    env->GetDoubleArrayRegion(matrix_b, 0, size, b.data());

    std::vector<double> result(size);
    for (int i = 0; i < size; i++) {
        if (b[i] == 0.0) {
            result[i] = std::numeric_limits<double>::quiet_NaN();
        } else {
            result[i] = a[i] / b[i];
        }
    }

    jdoubleArray resultArray = env->NewDoubleArray(size);
    env->SetDoubleArrayRegion(resultArray, 0, size, result.data());
    return resultArray;
}