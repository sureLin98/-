package com.example.coolweather.util;

import org.codehaus.jackson.map.ObjectMapper;

public class JacksonMapper {

        private static ObjectMapper mapper ;
        public static ObjectMapper getMapper(){
            if(mapper==null){
                synchronized (JacksonMapper.class){
                    if(mapper == null){
                        mapper = new ObjectMapper();
                    }
                }
            }
            return mapper;
        }
    }

