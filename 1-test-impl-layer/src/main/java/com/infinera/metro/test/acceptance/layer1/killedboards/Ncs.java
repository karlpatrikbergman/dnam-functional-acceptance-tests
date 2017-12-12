package com.infinera.metro.test.acceptance.layer1.killedboards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ncs {
    String sncId;
    String layerRate;
    String operStatus;
    String adminStatus;
    String aend;
    String zend;
}