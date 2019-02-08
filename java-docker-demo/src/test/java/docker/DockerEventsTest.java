package docker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.api.model.EventType;
import com.github.dockerjava.core.command.EventsResultCallback;
import demo.DockerClientHelper;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class DockerEventsTest {

    DockerClient docker;
    ObjectMapper objectMapper;

    @Before
    public void setUp() {
        this.docker = DockerClientHelper.INSTANCE.getDockerClient();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testEventStreaming() throws Exception {
        EventsResultCallback callback = docker.eventsCmd().exec(new EventsResultCallback() {
            public void onNext(Event item) {
                //item.get
                EventType type = item.getType();
                if (type != EventType.CONTAINER) {
                    return;
                }

                try {
                    logger.info("## Receive container event. \n{}",
                        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(item));
                } catch (Exception e) {
                    logger.warn("Failed to parse to json. {}", item.toString());
                }
            }
        });

        TimeUnit.MINUTES.sleep(2L);
        callback.close();
    }
}

/*
13:34:37.754 INFO [d.DockerEventsTest] ## Receive container event. Event[status=start,id=37a687553efa8403e7c3fc48d5c4e3363593c9946c07f85175bd5c336361ce5e,from=zookeeper,node=<null>,type=CONTAINER,action=start,actor=com.github.dockerjava.api.model.EventActor@642319ba[id=37a687553efa8403e7c3fc48d5c4e3363593c9946c07f85175bd5c336361ce5e,attributes={com.docker.compose.config-hash=bace34b3cdb739e2dcdfa3e5278ce7c1e5b395ecca40d2f24a0f799fb3b60df7, com.docker.compose.container-number=1, com.docker.compose.oneoff=False, com.docker.compose.project=zookeeper, com.docker.compose.service=zoo1, com.docker.compose.version=1.8.0, image=zookeeper, name=zookeeper_zoo1_1}],time=1549600551,timeNano=1549600551292253793]
13:34:47.675 INFO [d.DockerEventsTest] ## Receive container event. Event[status=kill,id=37a687553efa8403e7c3fc48d5c4e3363593c9946c07f85175bd5c336361ce5e,from=zookeeper,node=<null>,type=CONTAINER,action=kill,actor=com.github.dockerjava.api.model.EventActor@7ff6e974[id=37a687553efa8403e7c3fc48d5c4e3363593c9946c07f85175bd5c336361ce5e,attributes={com.docker.compose.config-hash=bace34b3cdb739e2dcdfa3e5278ce7c1e5b395ecca40d2f24a0f799fb3b60df7, com.docker.compose.container-number=1, com.docker.compose.oneoff=False, com.docker.compose.project=zookeeper, com.docker.compose.service=zoo1, com.docker.compose.version=1.8.0, image=zookeeper, name=zookeeper_zoo1_1, signal=15}],time=1549600561,timeNano=1549600561236805880]
13:34:48.082 INFO [d.DockerEventsTest] ## Receive container event. Event[status=die,id=37a687553efa8403e7c3fc48d5c4e3363593c9946c07f85175bd5c336361ce5e,from=zookeeper,node=<null>,type=CONTAINER,action=die,actor=com.github.dockerjava.api.model.EventActor@76cc1d5f[id=37a687553efa8403e7c3fc48d5c4e3363593c9946c07f85175bd5c336361ce5e,attributes={com.docker.compose.config-hash=bace34b3cdb739e2dcdfa3e5278ce7c1e5b395ecca40d2f24a0f799fb3b60df7, com.docker.compose.container-number=1, com.docker.compose.oneoff=False, com.docker.compose.project=zookeeper, com.docker.compose.service=zoo1, com.docker.compose.version=1.8.0, exitCode=143, image=zookeeper, name=zookeeper_zoo1_1}],time=1549600561,timeNano=1549600561642995307]
13:34:48.235 INFO [d.DockerEventsTest] ## Receive container event. Event[status=stop,id=37a687553efa8403e7c3fc48d5c4e3363593c9946c07f85175bd5c336361ce5e,from=zookeeper,node=<null>,type=CONTAINER,action=stop,actor=com.github.dockerjava.api.model.EventActor@2fd248fc[id=37a687553efa8403e7c3fc48d5c4e3363593c9946c07f85175bd5c336361ce5e,attributes={com.docker.compose.config-hash=bace34b3cdb739e2dcdfa3e5278ce7c1e5b395ecca40d2f24a0f799fb3b60df7, com.docker.compose.container-number=1, com.docker.compose.oneoff=False, com.docker.compose.project=zookeeper, com.docker.compose.service=zoo1, com.docker.compose.version=1.8.0, image=zookeeper, name=zookeeper_zoo1_1}],time=1549600561,timeNano=1549600561795876936]
 */

/*
13:36:12.919 INFO [d.DockerEventsTest] ## Receive container event.
{
  "status" : "start",
  "id" : "37a687553efa8403e7c3fc48d5c4e3363593c9946c07f85175bd5c336361ce5e",
  "from" : "zookeeper",
  "Type" : "container",
  "Action" : "start",
  "Actor" : {
    "ID" : "37a687553efa8403e7c3fc48d5c4e3363593c9946c07f85175bd5c336361ce5e",
    "Attributes" : {
      "com.docker.compose.config-hash" : "bace34b3cdb739e2dcdfa3e5278ce7c1e5b395ecca40d2f24a0f799fb3b60df7",
      "com.docker.compose.container-number" : "1",
      "com.docker.compose.oneoff" : "False",
      "com.docker.compose.project" : "zookeeper",
      "com.docker.compose.service" : "zoo1",
      "com.docker.compose.version" : "1.8.0",
      "image" : "zookeeper",
      "name" : "zookeeper_zoo1_1"
    }
  },
  "time" : 1549600646,
  "timeNano" : 1549600646453349427
}
13:36:15.788 INFO [d.DockerEventsTest] ## Receive container event.
{
  "status" : "kill",
  "id" : "37a687553efa8403e7c3fc48d5c4e3363593c9946c07f85175bd5c336361ce5e",
  "from" : "zookeeper",
  "Type" : "container",
  "Action" : "kill",
  "Actor" : {
    "ID" : "37a687553efa8403e7c3fc48d5c4e3363593c9946c07f85175bd5c336361ce5e",
    "Attributes" : {
      "com.docker.compose.config-hash" : "bace34b3cdb739e2dcdfa3e5278ce7c1e5b395ecca40d2f24a0f799fb3b60df7",
      "com.docker.compose.container-number" : "1",
      "com.docker.compose.oneoff" : "False",
      "com.docker.compose.project" : "zookeeper",
      "com.docker.compose.service" : "zoo1",
      "com.docker.compose.version" : "1.8.0",
      "image" : "zookeeper",
      "name" : "zookeeper_zoo1_1",
      "signal" : "15"
    }
  },
  "time" : 1549600649,
  "timeNano" : 1549600649348635555
}
13:36:16.180 INFO [d.DockerEventsTest] ## Receive container event.
{
  "status" : "die",
  "id" : "37a687553efa8403e7c3fc48d5c4e3363593c9946c07f85175bd5c336361ce5e",
  "from" : "zookeeper",
  "Type" : "container",
  "Action" : "die",
  "Actor" : {
    "ID" : "37a687553efa8403e7c3fc48d5c4e3363593c9946c07f85175bd5c336361ce5e",
    "Attributes" : {
      "com.docker.compose.config-hash" : "bace34b3cdb739e2dcdfa3e5278ce7c1e5b395ecca40d2f24a0f799fb3b60df7",
      "com.docker.compose.container-number" : "1",
      "com.docker.compose.oneoff" : "False",
      "com.docker.compose.project" : "zookeeper",
      "com.docker.compose.service" : "zoo1",
      "com.docker.compose.version" : "1.8.0",
      "exitCode" : "143",
      "image" : "zookeeper",
      "name" : "zookeeper_zoo1_1"
    }
  },
  "time" : 1549600649,
  "timeNano" : 1549600649740535419
}
13:36:16.336 INFO [d.DockerEventsTest] ## Receive container event.
{
  "status" : "stop",
  "id" : "37a687553efa8403e7c3fc48d5c4e3363593c9946c07f85175bd5c336361ce5e",
  "from" : "zookeeper",
  "Type" : "container",
  "Action" : "stop",
  "Actor" : {
    "ID" : "37a687553efa8403e7c3fc48d5c4e3363593c9946c07f85175bd5c336361ce5e",
    "Attributes" : {
      "com.docker.compose.config-hash" : "bace34b3cdb739e2dcdfa3e5278ce7c1e5b395ecca40d2f24a0f799fb3b60df7",
      "com.docker.compose.container-number" : "1",
      "com.docker.compose.oneoff" : "False",
      "com.docker.compose.project" : "zookeeper",
      "com.docker.compose.service" : "zoo1",
      "com.docker.compose.version" : "1.8.0",
      "image" : "zookeeper",
      "name" : "zookeeper_zoo1_1"
    }
  },
  "time" : 1549600649,
  "timeNano" : 1549600649896596467
}
 */
