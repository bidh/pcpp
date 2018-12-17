/*
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.sun.org.apache.xml.internal.security.Init;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class InitMessage implements Serializable{
    final ActorRef Registry;
    public InitMessage(ActorRef Registry){
        this.Registry=Registry;
    }
}
class CommMessage implements Serializable{
    final ActorRef Receiver;
    public CommMessage(ActorRef Receiver){
        this.Receiver=Receiver;
    }
}
class PublicKeyMessage implements Serializable{
    final ActorRef Reciever;
    final int publickey;

    public PublicKeyMessage(ActorRef Receiver, int publickey){
        this.Reciever=Receiver;
        this.publickey=publickey;
    }
}
class KeyPair implements Serializable {
    public final int public_key, private_key;
    public KeyPair(int public_key, int private_key) {
        this.public_key = public_key;
        this.private_key = private_key;
    }
}
class RegisterMessage implements Serializable{
    final ActorRef receiver;

    public RegisterMessage(ActorRef receiver){
        this.receiver=receiver;
    }
}
class LookupMessage implements Serializable{
    final ActorRef receiver;

    public LookupMessage(ActorRef receiver){
        this.receiver=receiver;
    }
}
class Message implements Serializable{
    final String message;

    public Message(String message){
        this.message=message;
    }
}
class RegistryActor extends UntypedActor{
    Map<ActorRef,Integer> KeysMap=new HashMap<ActorRef, Integer>();
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof RegisterMessage){
            RegisterMessage registerMessage=(RegisterMessage)o;
            KeyPair keyPair=Crypto.keygen();
            registerMessage.receiver.tell(new KeyPair(keyPair.public_key,keyPair.private_key),ActorRef.noSender());
            KeysMap.put(registerMessage.receiver,keyPair.public_key);
        }
        if(o instanceof LookupMessage){
            LookupMessage lookupMessage=(LookupMessage)o;
            getSender().tell(new PublicKeyMessage(lookupMessage.receiver,KeysMap.get(lookupMessage.receiver)),ActorRef.noSender());
        }
    }
}
class SenderActor extends UntypedActor{

    ActorRef Registry;
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof InitMessage){
            InitMessage initMessage=(InitMessage)o;
            this.Registry=initMessage.Registry;
        }
        if(o instanceof CommMessage){
            CommMessage commMessage=(CommMessage)o;
            Registry.tell(new LookupMessage(commMessage.Receiver),getSelf());
        }
        if(o instanceof PublicKeyMessage){
            PublicKeyMessage publicKeyMessage=(PublicKeyMessage)o;
            String X="SECRET";
            System.out.println("cleartext: "+X);
            String Y=Crypto.encrypt(X, publicKeyMessage.publickey);
            System.out.println("encrypted: "+Y);
            publicKeyMessage.Reciever.tell(new Message(Y),ActorRef.noSender());
        }
    }
}
class ReceiverActor extends UntypedActor{
    ActorRef Registry;
    int publickey,privatekey;
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof InitMessage){
            InitMessage initMessage=(InitMessage)o;
            initMessage.Registry.tell(new RegisterMessage(getSelf()),ActorRef.noSender());
            this.Registry=initMessage.Registry;
        }
        if(o instanceof KeyPair){
            KeyPair keyPair=(KeyPair)o;
            publickey=keyPair.public_key;
            privatekey=keyPair.private_key;
        }
        if(o instanceof Message){
            Message message=(Message)o;
            String X=Crypto.encrypt(message.message,privatekey);
            System.out.println("decrypted key: "+X);
        }
    }
}
class Crypto {
    static KeyPair keygen() {
        int public_key = (new Random()).nextInt(25)+1;
        int private_key = 26 - public_key;
        System.out.println("public key: " + public_key);
        System.out.println("private key: " + private_key);
        return new KeyPair(public_key, private_key);
    }
    static String encrypt(String cleartext, int key) {
        StringBuffer encrypted = new StringBuffer();
        for (int i=0; i<cleartext.length(); i++) {
            encrypted.append((char) ('A' + ((((int)
                    cleartext.charAt(i)) - 'A' + key) % 26)));
        }
        return "" + encrypted;
    }
}
public class SecComSys {
    public static void main(String[] args){
        final ActorSystem system= ActorSystem.create("SecConSysSystem");
        final ActorRef RegistryActor=system.actorOf(Props.create(RegistryActor.class),"RegisterActor");
        final ActorRef ReceiverActor=system.actorOf(Props.create(ReceiverActor.class),"ReceiverActor");

        ReceiverActor.tell(new InitMessage(RegistryActor),ActorRef.noSender());

        final ActorRef SenderActor=system.actorOf(Props.create(SenderActor.class),"SenderActor");

        SenderActor.tell(new InitMessage(RegistryActor),ActorRef.noSender());
        SenderActor.tell(new CommMessage(ReceiverActor),ActorRef.noSender());
    }
}
*/
