package com.xinra.growthlectures.service;

import com.xinra.nucleus.interfacegenerator.GenerateInterface;
import com.xinra.nucleus.interfacegenerator.InterfaceNamingStrategy;
import com.xinra.nucleus.service.ServiceImpl;

/**
 * Intermediate super type for all services. Workaround for
 * https://github.com/xinra-nucleus/nucleus-interface-generator/issues/5
 */
@GenerateInterface(namingStrategy = InterfaceNamingStrategy.EXCEPT_LAST_FOUR_CHARS)
public abstract class GrowthlecturesServiceImpl extends ServiceImpl {

}
