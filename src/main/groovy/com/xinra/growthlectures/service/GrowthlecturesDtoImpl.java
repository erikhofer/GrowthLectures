package com.xinra.growthlectures.service;

import com.xinra.nucleus.interfacegenerator.GenerateInterface;
import com.xinra.nucleus.interfacegenerator.InterfaceNamingStrategy;
import com.xinra.nucleus.service.DtoImpl;

/**
 * Intermediate super type for all DTOs. Workaround for
 * https://github.com/xinra-nucleus/nucleus-interface-generator/issues/5
 */
@GenerateInterface(
      namingStrategy = InterfaceNamingStrategy.EXCEPT_LAST_FOUR_CHARS,
      propertyConstants = true
    )
public abstract class GrowthlecturesDtoImpl extends DtoImpl {

}
