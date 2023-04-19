package com.rpc.codec;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.rpc.common.RPCRequest;
import com.rpc.common.RPCResponse;
import lombok.var;

import javax.sql.rowset.serial.SerialException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 这次用kryo看看优化效果
 */
public class KryoSerializer implements Serializer{

    //kryo并非是线程安全，所以要用ThreadLocal 来储存kryo objects
    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() ->{
        Kryo kryo = new Kryo();
        kryo.register(RPCResponse.class);
        kryo.register(RPCRequest.class);
        return kryo;
    });
    @Override
    public byte[] serialize(Object obj) {
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Output output = new Output(byteArrayOutputStream)){
            Kryo kryo = kryoThreadLocal.get();
            //Object->byte:将对象序列化为byte数组
            kryo.writeObject(output,obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (IOException e) {
            System.out.println("Kryo序列化失败");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            Object o;

            switch (messageType) {
                case 0: // 如果是 request
                    o = kryo.readObject(input, RPCRequest.class);
                    break;
                case 1: // 如果是 response
                    o = kryo.readObject(input, RPCResponse.class);
                    break;
                default:
                    System.out.println("暂时不支持此种消息");
                    throw new RuntimeException();
            }

            kryoThreadLocal.remove();
            return o;
        } catch (IOException e) {
            System.out.println("Kryo反序列化失败");
            throw new RuntimeException(e);
        }
    }




    @Override
    public int getType() {
        return 2;
    }
}
