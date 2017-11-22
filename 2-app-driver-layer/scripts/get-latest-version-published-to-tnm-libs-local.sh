#!/usr/bin/env bash

source "${BASH_SOURCE%/*}/lib/assert-jq-installed"
source "${BASH_SOURCE%/*}/lib/common-settings"
source "${BASH_SOURCE%/*}/lib/common-functions"

get_tnm_libs_local_published_today() {
    local readonly TODAY=$(date +"%Y-%m-%d")
    local readonly FROM="${TODAY}T00:00:00.000Z"
    curl -sSL -uadmin:Infinera99! -X GET "http://se-artif-prd.infinera.com/artifactory/api/search/dates?from=${FROM}&dateFields=created&repos=tnm-libs-local"
}

filter_out_only_latest_dnam_version() {
    local readonly LATEST_DEPLOYED_VERSION=$(trim_leading_and_ending_double_quotes $(get_latest_deployed_dnam_version))
    jq --arg LATEST_DEPLOYED_VERSION "${LATEST_DEPLOYED_VERSION}" '.results | { results: map(select(.uri | contains($LATEST_DEPLOYED_VERSION))) }'
}

get_latest_pom_published_to_tnm_libs_local() {
    jq '.results | sort_by(.created) | map(select(.uri | endswith(".pom"))) | last'
}

get_latest_version_published_to_tnm_libs_local() {
    local readonly INPUT;
    read -d '' -u 0 INPUT;
    local readonly TRIM_STRING_BEFORE_RIGHTMOST_DASH="${INPUT##*-}"
    local readonly TRIM_STRING_AFTER_RIGHTMOST_DOT="${TRIM_STRING_BEFORE_RIGHTMOST_DASH%.*}"
    printf "$@" "${TRIM_STRING_AFTER_RIGHTMOST_DOT}"
}

get_latest_git_hash_from_latest_version() {
    local readonly INPUT;
    read -d '' -u 0 INPUT;
    local readonly TRIM_STRING_BEFORE_RIGHTMOST_DOT="${INPUT##*.}"

    if (! string_has_expected_length "${TRIM_STRING_BEFORE_RIGHTMOST_DOT}" || ! is_alpha_numeric "${TRIM_STRING_BEFORE_RIGHTMOST_DOT}"); then
        printf "None";
    else
        printf "$@" "${TRIM_STRING_BEFORE_RIGHTMOST_DOT}"
    fi
}

directory_uri_contains_git_hash() {
    local readonly DIR_URI="${1}"
    local readonly GIT_HASH="${2}"
    [ "${DIR_URI/${GIT_HASH}}" != "${DIR_URI}" ]
}