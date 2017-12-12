package com.infinera.metro.test.acceptance.layer1.killedboards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by stos on 03/10/16.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NcId {
    String ncCount;
    List<Ncs> restNCs;


}
