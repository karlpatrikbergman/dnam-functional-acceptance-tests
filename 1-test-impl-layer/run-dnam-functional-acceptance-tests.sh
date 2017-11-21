#!/usr/bin/env bash

# ./run-dnam-functional-acceptance-tests.sh se.infinera.metro.test.acceptance.layer2

get_test_package() {
    local readonly INPUT="${1}"
    case ${INPUT} in
    all)
        echo "com.infinera.metro.test.acceptance"
        ;;
    layer1)
        echo "com.infinera.metro.test.acceptance.layer1"
        ;;
    layer2)
        echo "com.infinera.metro.test.acceptance.layer2"
        ;;
    *)
        echo "${INPUT}"
    esac
}

run_tests() {
    local readonly TEST_PACKAGE=$(get_test_package "${1}")
    exec java -cp "build/libs/junit-platform-console-standalone-1.0.2.jar:build/libs/*" org.junit.platform.console.ConsoleLauncher \
    --scan-class-path "build/libs/1-test-impl-layer-1.0.jar" \
    --include-package ${TEST_PACKAGE} \
    --exclude-engine junit-vintage
}

if [ ! -d "build/libs/" ]; then
    printf "'build/libs' does not exist. Build tests from top level with './gradlew clean build'\n"  >&2
    exit 1
elif [ "$#" -ne 1 ]; then
    printf "Usage: ${FUNCNAME[0]} <test-package>\n"  >&2
    exit 1
else
    printf "Running: ${FUNCNAME[0]} ${1}\n"
    run_tests "${1}"
fi

unset -f get_test_package
unset -f run_tests
