package com.carrot.service;

import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = {Exception.class})
public abstract class CarrotService {



}
