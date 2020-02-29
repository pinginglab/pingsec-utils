#!/bin/bash
########################### PROJECT VARIABLES #####################
PROJECT_NAME="utils"
PROFILE="prod"
########################## COLORS SIGN ############################
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'
###################################################################


case $# in
    0)
        echo -e "default projectName: ${GREEN}${PROJECT_NAME}${NC}, profile: ${GREEN}${PROFILE}${NC}"
        ;;
    1)
        if [ "-h" == $1 ]; then
            echo -e "usage: ${GREEN}./buildDocker [prod] [projectName]"
            exit
        fi
        PROFILE=$1
        ;;
    2)
        PROFILE=$1
        PROJECT_NAME=$2
        ;;
    *)
        echo "TOO MUCH ARGUMENTS"
        exit
        ;;
esac


echo -e "build docker with projectName: ${BLUE}${PROJECT_NAME}${NC}, profile: ${BLUE}${PROFILE}${NC}"
./gradlew bootJar -P${PROFILE}  jibDockerBuild
if [ $? != 0 ]; then
    echo -e "${RED}build dokcer failed${NC} check your config or pwd"
    exit
fi

LAST_DATE_TAG=`docker images ${PROJECT_NAME} | awk '{print $2}' | grep ${PROFILE} | sort -n -r  | head -n 1`
echo -e "USING THE TAG: ${BLUE}${LAST_DATE_TAG} ${NC}"
docker tag ${PROJECT_NAME}:${LAST_DATE_TAG} ccr.ccs.tencentyun.com/compose/${PROJECT_NAME}:${LAST_DATE_TAG}
docker push ccr.ccs.tencentyun.com/compose/${PROJECT_NAME}:${LAST_DATE_TAG}


##!/bin/bash
#./gradlew bootWar -Pprod buildDocker
#docker tag usertasks:latest registry.cmic.site:5000/sg/usertasks:latest
#docker push registry.cmic.site:5000/sg/usertasks:latest
#sh "curl -u \"428B2060F8F2AFECB66E:Wvmby5ste2rPufT184Wo8TEULWGSW9tQfb1NJZKy\" \\\n" +
#            "-X POST \\\n" +
#            "-H 'Accept: application/json' \\\n" +
#            "-H 'Content-Type: application/json' \\\n" +
#            "-d '{\"inServiceStrategy\":{\"batchSize\":1,\"intervalMillis\":2000,\"startFirst\":false,\"launchConfig\":{\"instanceTriggeredStop\":\"stop\",\"kind\":\"container\",\"networkMode\":\"managed\",\"privileged\":false,\"publishAllPorts\":false,\"readOnly\":false,\"runInit\":false,\"startOnCreate\":true,\"stdinOpen\":false,\"tty\":false,\"vcpu\":1,\"drainTimeoutMs\":0,\"type\":\"launchConfig\",\"blkioWeight\":null,\"capAdd\":[],\"capDrop\":[],\"cgroupParent\":null,\"count\":null,\"cpuCount\":null,\"cpuPercent\":null,\"cpuPeriod\":null,\"cpuQuota\":null,\"cpuRealtimePeriod\":null,\"cpuRealtimeRuntime\":null,\"cpuSet\":null,\"cpuSetMems\":null,\"cpuShares\":null,\"dataVolumes\":[],\"dataVolumesFrom\":[],\"description\":null,\"devices\":[],\"diskQuota\":null,\"dns\":[],\"dnsSearch\":[],\"domainName\":null,\"environment\":{\"JHIPSTER_SLEEP\":\"0\",\"SPRING_CLOUD_CONSUL_HOST\":\"consul.cmic.site\",\"SPRING_CLOUD_CONSUL_PORT\":\"8500\",\"SPRING_PROFILES_ACTIVE\":\"prod,swagger\"},\"healthInterval\":null,\"healthRetries\":null,\"healthTimeout\":null,\"hostname\":null,\"imageUuid\":\"docker:registry.cmic.site:5000/sg/security\",\"ioMaximumBandwidth\":null,\"ioMaximumIOps\":null,\"ip\":null,\"ip6\":null,\"ipcMode\":null,\"isolation\":null,\"kernelMemory\":null,\"labels\":{\"io.rancher.container.pull_image\":\"always\"},\"logConfig\":{\"type\":\"logConfig\",\"config\":{},\"driver\":null},\"memory\":null,\"memoryMb\":null,\"memoryReservation\":null,\"memorySwap\":null,\"memorySwappiness\":null,\"milliCpuReservation\":null,\"oomScoreAdj\":null,\"pidMode\":null,\"pidsLimit\":null,\"ports\":[\"8088:8088/tcp\"],\"requestedIpAddress\":null,\"secrets\":[],\"shmSize\":null,\"stopSignal\":null,\"stopTimeout\":null,\"system\":false,\"user\":null,\"userdata\":null,\"usernsMode\":null,\"uts\":null,\"version\":\"4f6515d3-40a9-423a-9bfb-2b4afaa2b55e\",\"volumeDriver\":null,\"workingDir\":null,\"dataVolumesFromLaunchConfigs\":[],\"networkLaunchConfig\":null,\"createIndex\":null,\"created\":null,\"deploymentUnitUuid\":null,\"externalId\":null,\"firstRunning\":null,\"healthState\":null,\"removed\":null,\"startCount\":null,\"uuid\":null},\"secondaryLaunchConfigs\":[]}}' \\\n" +
#            "'https://rancher.cmic.site/v2-beta/projects/1a9/services/1s97/?action=upgrade'"
