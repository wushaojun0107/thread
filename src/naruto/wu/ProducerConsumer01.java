package naruto.wu;
//java多线程模拟生产者消费者问题
//ProducerConsumer是主类，Producer生产者，Consumer消费者，Product产品
//Storage仓库
public class ProducerConsumer01 {

	public static void main(String[] args) {
		Storage s = new Storage();
		
		Producer p = new Producer(s);
		Consumer c = new Consumer(s);
		
		Thread producer = new Thread(p);
		Thread consumer = new Thread(c);
		producer.start();
		consumer.start();

	}
}
// 消费者
class Consumer implements Runnable {	
	Storage s = null;
	public Consumer(Storage s){
		this.s = s;
	}
	public void run() {
		for(int i=0; i<20; i++){
			Product p = s.pop();//取出产品
			try {
				Thread.sleep((int)(Math.random()*1500));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
// 生产者
class Producer implements Runnable {
	Storage s = null;
	
	public Producer(Storage s){
		this.s = s;
	}

	public void run() {
		for(int i=0; i<20; i++){
			Product p = new Product(i);
			s.push(p);	//放入产品
//			System.out.println("生产者放入：" + p);
			try {
				Thread.sleep((int)(Math.random()*1500));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}

class Product {
	int id;
	
	public Product(int id){
		this.id = id;
	}
	//	重写toString方法
	public String toString(){
		return "产品："+this.id;
	}
}


class Storage {
	int index = 0;
	Product[] products = new Product[5];
	//	放入
	public synchronized void push(Product p){
		while(index==this.products.length){
			//	这时候表示仓库已经满了
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.products[index] = p;
		System.out.println("生产者放入"+index+"位置：" + p);
		index++;
		this.notifyAll();
	}
	//	取出
	public synchronized Product pop(){
		while(this.index==0){
			//	这时候表示仓库已经空了
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		index--;
		this.notifyAll();
		System.out.println("消费者从"+ index+ "位置取出：" + this.products[index]);
		return this.products[index];
	}
}
