import akka.actor.UntypedActor;

import java.io.Serializable;

class StartTransferMessage implements Serializable{

}
class TransferMessage implements Serializable{

}
class DepositMessage implements Serializable{

}
class PrintBalanceMessage implements Serializable{

}

class BankActor extends UntypedActor{

    @Override
    public void onReceive(Object o) throws Exception {

    }
}
class AccountActor extends UntypedActor{

    @Override
    public void onReceive(Object o) throws Exception {

    }
}
class ClerkActor extends UntypedActor{

    @Override
    public void onReceive(Object o) throws Exception {

    }
}
public class ABC {

}
