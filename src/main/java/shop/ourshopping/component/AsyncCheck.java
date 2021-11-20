package shop.ourshopping.component;

import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

// 비동기 멀티 스레드를 통하여 로그인 실시간 체크(미구현)
@EnableAsync
@Component
public class AsyncCheck {

	private String taskId;
	private AtomicBoolean inProgress = new AtomicBoolean(false);

	// 비동기로 동작하는 메소드
	@Async
	public Future<String> process() {
		taskId = UUID.randomUUID().toString();
		if (inProgress.compareAndSet(false, true)) {
			while (true) {
				if (Thread.currentThread().isInterrupted()) {
					System.out.println("Cancelled");
					inProgress.set(false);

					return new AsyncResult<String>(taskId);
				}

				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return new AsyncResult<String>(taskId);
				}
			}
		}

		return new AsyncResult<String>(taskId);
	}
}