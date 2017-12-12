package com.infinera.metro.test.acceptance.layer1.killedboards;

import com.github.rholder.retry.*;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infinera.metro.dnam.acceptance.test.util.ThreadSleepWrapper;
import com.infinera.metro.networkmanager.tools.docker.DockerUtil;
import com.infinera.metro.networkmanager.tools.wait.Wait;
import com.infinera.metro.test.acceptance.MetroManagementDslTest;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import se.transmode.tnm.mtosi.model.NamingAttribute;
import se.transmode.tnm.mtosi.model.enums.LayerRate;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static se.transmode.tnm.mtosi.model.enums.LayerRate.*;

@ExtendWith(KilledBoardsTestDataConfigurer.class)
@Slf4j
public class NetworkConnectionRestAssuredTest extends MetroManagementDslTest {
    public static final String DEFAULT_HOST = "tnm-net-test";
    final String apiNodes = "/api/node/admin";
    final String apiNcs = "/v1/networkconnections";
    final String apiRouteinfo = "/api/export/trace/routeinfo";
    final String nbiTPControl = "/soap/mtosi/TerminationPointControl";
    final int portNbi = 8080;
    static final String hostname = System.getProperty("restHost", DEFAULT_HOST);
    static final int portEmbedded = 8888;
    static final int portSpringboot = hostname.equals("localhost")?8091:8081;
    static final String NODE_A = "OTN-PP-A";
    static final String NODE_B = "OTN-PP-B";
    static final String NODE_C = "OTN-HANDOFF";

    Set<String> KILLED_BOARD_NODES = Sets.newHashSet(node1.getIpAddress(), node2.getIpAddress());

    private Map<String, String> nodeName2Ip;
    private Map<String, String> endPoint2SncId;
    List<Ncs> networkConnections;

    @BeforeEach
    public void setup() throws InterruptedException, DockerException, DockerCertificateException {

        log.info("######## Running test TopologyAcceptanceTest");

        nodeApi.assertNodeNotAdded(node1);
        nodeApi.assertNodeNotAdded(node2);

        nodeApi.addNode(node1);
        nodeApi.addNode(node2);

        baseURI = "http://" + DockerUtil.DOCKER_UTIL.getContainerIpAddress("dnam-mainserver");
        authentication = preemptive().basic("admin", "admin");
        init();
    }

    @AfterEach
    public void teardown() {
        nodeApi.deleteNode(node1);
        nodeApi.deleteNode(node2);
    }
    private void init() {
        nodeName2Ip = new HashMap<>();
        endPoint2SncId = new HashMap<>();
        port = portSpringboot;
        basePath = apiNodes;
        Response availableNodes = new Wait().perform(() -> when().get().then().contentType(ContentType.JSON).extract().response());
        List<MeId> meIds = Arrays.asList(availableNodes.getBody().as(MeId[].class));
        meIds.stream()
             .forEach(meId -> nodeName2Ip.put(Strings.isNullOrEmpty(meId.getNodeName()) ? meId.getNodeIp() : (meId.getNodeName()), meId.getNodeIp()));
        if (nodeName2Ip.isEmpty()) {
            Assert.fail("No nodes in the system...something has gone wrong initializing the test.");
        }
        port = portEmbedded;
        basePath = apiNcs;

        NcId ncs = getNcId();

        networkConnections = Lists.newArrayList();
        ncs.getRestNCs().stream().forEach(nc -> {
            endPoint2SncId.put(nc.getAend(), nc.getSncId());
            endPoint2SncId.put(nc.getZend(), nc.getSncId());
            networkConnections.add(nc);
        });
        if (endPoint2SncId.isEmpty()) {
            Assert.fail("Oh my!!...there doesn't appear to be any snc's in the system...best to give up now");
        }
    }

    @Test
    public void test_killed_boards_number_of_NC() {

        log.info("test_killed_boards_number_of_NC");

        assertThat("NC in any layer ", numberOfNcsWithLayerRate(KILLED_BOARD_NODES, LR_Not_Applicable), is(equalTo(92)));
        assertThat("NC in layer LR_Optical_Channel", numberOfNcsWithLayerRate(KILLED_BOARD_NODES, LR_Optical_Channel), is(equalTo(70)));
        assertThat("NC in layer LR_DSR_10Gigabit_Ethernet", numberOfNcsWithLayerRate(KILLED_BOARD_NODES, LR_DSR_10Gigabit_Ethernet), is(equalTo(2)));
        assertThat("NC in layer LR_STS3c_and_AU4_VC4", numberOfNcsWithLayerRate(KILLED_BOARD_NODES, LR_STS3c_and_AU4_VC4), is(equalTo(19)));
        assertThat("NC in layer LR_FC_200_2126M", numberOfNcsWithLayerRate(KILLED_BOARD_NODES, LR_FC_200_2126M), is(equalTo(1)));

        Set<LayerRate> expectedLayerRates = Sets.newHashSet(LR_Optical_Channel, LR_DSR_10Gigabit_Ethernet, LR_STS3c_and_AU4_VC4, LR_FC_200_2126M);
        assertThat(unexpectedLayerRates(KILLED_BOARD_NODES, expectedLayerRates), is(Sets.newHashSet()));
        assertThat("There should be no unexpected layer rates", unexpectedLayerRates(KILLED_BOARD_NODES, expectedLayerRates).isEmpty());
    }

    @Disabled
    @Test
    public void test_otn_boards_number_of_NC() {
        Set<String> otnNodes = Sets.newHashSet(nodeName2Ip.get(NODE_A), nodeName2Ip.get(NODE_B), nodeName2Ip.get(NODE_C));
        assertThat("NC in any layer", numberOfNcsWithLayerRate(otnNodes, LR_Not_Applicable), is(equalTo(202)));
        assertThat("NC in layer LR_Optical_Channel", numberOfNcsWithLayerRate(otnNodes, LR_Optical_Channel), is(equalTo(154)));
        assertThat("NC in layer LR_Ethernet", numberOfNcsWithLayerRate(otnNodes, LR_Ethernet), is(equalTo(12)));
        assertThat("NC in layer LR_OCH_Data_Unit_0", numberOfNcsWithLayerRate(otnNodes, LR_OCH_Data_Unit_0), is(equalTo(14)));
        assertThat("NC in layer LR_OCH_Data_Unit_2", numberOfNcsWithLayerRate(otnNodes, LR_OCH_Data_Unit_2), is(equalTo(3)));
        assertThat("NC in layer LR_OCH_Data_Unit_2e", numberOfNcsWithLayerRate(otnNodes, LR_OCH_Data_Unit_2e), is(equalTo(18)));
        assertThat("NC in layer LR_OCH_Data_Unit_4", numberOfNcsWithLayerRate(otnNodes, LR_OCH_Data_Unit_4), is(equalTo(1)));

        Set<LayerRate> expectedLayerRates = Sets.newHashSet(LR_Optical_Channel, LR_Ethernet, LR_OCH_Data_Unit_0, LR_OCH_Data_Unit_2,
                                                            LR_OCH_Data_Unit_2e, LR_OCH_Data_Unit_4);
        assertThat(unexpectedLayerRates(otnNodes, expectedLayerRates), is(Sets.newHashSet()));
        assertThat("There should be no unexpected layer rates", unexpectedLayerRates(otnNodes, expectedLayerRates).isEmpty());
    }

    private int numberOfNcsWithLayerRate(Set<String> nodes, LayerRate layerRate) {
        int ncCounter = 0;
        try {
            for (Ncs nc : networkConnections) {
                if ((layerRate == LR_Not_Applicable || nc.getLayerRate().equals(layerRate.toString())) && (nodes.contains(
                    NamingAttribute.of(nc.getAend()).getMeValue()) || nodes.contains(NamingAttribute.of(nc.getZend()).getMeValue()))) {
                    ncCounter++;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ncCounter;
    }

    private Set<LayerRate> unexpectedLayerRates(Set<String> nodes, Set<LayerRate> expectedLayerRates) {
        int ncCounter = 0;
        Set<LayerRate> unexpectedLayerRates = Sets.newHashSet();
        try {
            for (Ncs nc : networkConnections) {
                if (nodes.contains(NamingAttribute.of(nc.getAend()).getMeValue()) || nodes.contains(NamingAttribute.of(nc.getZend()).getMeValue())) {
                    LayerRate layerRate = LayerRate.valueOf(nc.getLayerRate());
                    if (!expectedLayerRates.contains(layerRate)) {
                        ncCounter++;
                        unexpectedLayerRates.add(layerRate);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return unexpectedLayerRates;
    }

    private NcId getNcId() {

        ThreadSleepWrapper.sleep(10); //TODO: How do we know when calculation is finished?

        Retryer<Response> retryer = RetryerBuilder.<Response>newBuilder()
            .retryIfResult(Objects::isNull)
            .retryIfResult(response -> response.getBody().as(NcId.class).ncCount.equals("0"))
            .retryIfResult(response -> response.getBody().as(NcId.class).restNCs == null)
            .retryIfException()
            .retryIfRuntimeException()
            .withWaitStrategy(WaitStrategies.exponentialWait(1000, 2, TimeUnit.SECONDS))
            .withStopStrategy(StopStrategies.stopAfterAttempt(300))
            .build();

        try {
            Response response = retryer.call(() -> given().param("topLevel", "true").when().get().then().contentType(ContentType.JSON).extract().response());
            return response.getBody().as(NcId.class);
        } catch (ExecutionException | RetryException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
