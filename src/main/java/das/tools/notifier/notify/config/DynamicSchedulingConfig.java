package das.tools.notifier.notify.config;

import das.tools.notifier.notify.service.RemoveOldFilesService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
@Configuration
@EnableScheduling
@EnableAsync
public class DynamicSchedulingConfig implements SchedulingConfigurer {
    private final RemoveOldFilesService removeService;
    public DynamicSchedulingConfig(RemoveOldFilesService removeService) {
        this.removeService = removeService;
    }

    @Bean
    public Executor taskExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (removeService.isServiceActive()) {
            taskRegistrar.setScheduler(taskExecutor());
            taskRegistrar.addTriggerTask(
                    new Runnable() {
                        @Override
                        public void run() {
                            removeService.scheduleOldFilesRemove();
                        }
                    },
                    new Trigger() {
                        @Override
                        public Date nextExecutionTime(TriggerContext context) {
                            Optional<Date> lastCompletionTime =
                                    Optional.ofNullable(context.lastCompletionTime());
                            Instant nextExecutionTime =
                                    lastCompletionTime.orElseGet(Date::new).toInstant()
                                            .plusMillis(removeService.getInterval());
                            return Date.from(nextExecutionTime);
                        }
                    }
            );
        }
    }
}
