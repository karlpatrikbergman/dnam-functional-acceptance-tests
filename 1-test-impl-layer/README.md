How to run test fixture in the background and tests docker container from command line

./gradlew clean build -x test
docker build -t dnam-acceptance-test . ; docker run --network 1testimpllayer_dnam_func_acc_test_layer1 --rm dnam-acceptance-test layer1

