#!/usr/bin/env bash

source "${BASH_SOURCE%/*}/get-latest-version-published-to-tnm-libs-local.sh"

#get_latest_deployed_dnam_version() {
#    local readonly LATEST_DNAM_VERSION=$(curl -s -uadmin:admin -X GET http://tnm:8081/api/version | jq '.[] | .version')
#    echo "${LATEST_DNAM_VERSION}"
#}
#get_latest_deployed_dnam_version

get_dnam_latest_published_version() {
    get_tnm_libs_local_published_today | \
        filter_out_only_latest_dnam_version | \
            get_latest_pom_published_to_tnm_libs_local | \
                jq '.uri' | \
                    get_latest_version_published_to_tnm_libs_local

}
get_dnam_latest_published_version
