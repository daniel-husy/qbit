/*******************************************************************************

  * Copyright (c) 2015. Rick Hightower, Geoff Chandler
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *  		http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  *  ________ __________.______________
  *  \_____  \\______   \   \__    ___/
  *   /  / \  \|    |  _/   | |    |  ______
  *  /   \_/.  \    |   \   | |    | /_____/
  *  \_____\ \_/______  /___| |____|
  *         \__>      \/
  *  ___________.__                  ____.                        _____  .__                                             .__
  *  \__    ___/|  |__   ____       |    |____ ___  _______      /     \ |__| ___________  ____  ______ ______________  _|__| ____  ____
  *    |    |   |  |  \_/ __ \      |    \__  \\  \/ /\__  \    /  \ /  \|  |/ ___\_  __ \/  _ \/  ___// __ \_  __ \  \/ /  |/ ___\/ __ \
  *    |    |   |   Y  \  ___/  /\__|    |/ __ \\   /  / __ \_ /    Y    \  \  \___|  | \(  <_> )___ \\  ___/|  | \/\   /|  \  \__\  ___/
  *    |____|   |___|  /\___  > \________(____  /\_/  (____  / \____|__  /__|\___  >__|   \____/____  >\___  >__|    \_/ |__|\___  >___  >
  *                  \/     \/                \/           \/          \/        \/                 \/     \/                    \/    \/
  *  .____    ._____.
  *  |    |   |__\_ |__
  *  |    |   |  || __ \
  *  |    |___|  || \_\ \
  *  |_______ \__||___  /
  *          \/       \/
  *       ____. _________________    _______         __      __      ___.     _________              __           __      _____________________ ____________________
  *      |    |/   _____/\_____  \   \      \       /  \    /  \ ____\_ |__  /   _____/ ____   ____ |  | __ _____/  |_    \______   \_   _____//   _____/\__    ___/
  *      |    |\_____  \  /   |   \  /   |   \      \   \/\/   // __ \| __ \ \_____  \ /  _ \_/ ___\|  |/ // __ \   __\    |       _/|    __)_ \_____  \   |    |
  *  /\__|    |/        \/    |    \/    |    \      \        /\  ___/| \_\ \/        (  <_> )  \___|    <\  ___/|  |      |    |   \|        \/        \  |    |
  *  \________/_______  /\_______  /\____|__  / /\    \__/\  /  \___  >___  /_______  /\____/ \___  >__|_ \\___  >__| /\   |____|_  /_______  /_______  /  |____|
  *                   \/         \/         \/  )/         \/       \/    \/        \/            \/     \/    \/     )/          \/        \/        \/
  *  __________           __  .__              __      __      ___.
  *  \______   \ ____   _/  |_|  |__   ____   /  \    /  \ ____\_ |__
  *  |    |  _// __ \  \   __\  |  \_/ __ \  \   \/\/   // __ \| __ \
  *   |    |   \  ___/   |  | |   Y  \  ___/   \        /\  ___/| \_\ \
  *   |______  /\___  >  |__| |___|  /\___  >   \__/\  /  \___  >___  /
  *          \/     \/             \/     \/         \/       \/    \/
  *
  * QBit - The Microservice lib for Java : JSON, WebSocket, REST. Be The Web!
  *  http://rick-hightower.blogspot.com/2014/12/rise-of-machines-writing-high-speed.html
  *  http://rick-hightower.blogspot.com/2014/12/quick-guide-to-programming-services-in.html
  *  http://rick-hightower.blogspot.com/2015/01/quick-start-qbit-programming.html
  *  http://rick-hightower.blogspot.com/2015/01/high-speed-soa.html
  *  http://rick-hightower.blogspot.com/2015/02/qbit-event-bus.html

 ******************************************************************************/

package io.advantageous.qbit.example.events;

import io.advantageous.qbit.QBit;
import io.advantageous.qbit.annotation.EventChannel;
import io.advantageous.qbit.annotation.OnEvent;
import io.advantageous.qbit.annotation.QueueCallback;
import io.advantageous.qbit.annotation.QueueCallbackType;
import io.advantageous.qbit.events.EventBusProxyCreator;
import io.advantageous.qbit.events.EventManager;
import io.advantageous.qbit.service.Service;
import io.advantageous.qbit.service.ServiceBuilder;
import io.advantageous.qbit.system.QBitSystemManager;
import org.boon.core.Sys;

import static io.advantageous.qbit.service.ServiceBuilder.serviceBuilder;
import static io.advantageous.qbit.service.ServiceProxyUtils.flushServiceProxy;
import static org.boon.Boon.puts;

/**
 * Created by rhightower on 2/11/15.
 */
public class UsingShutDown {

    public static final String NEW_HIRE_CHANNEL = "com.mycompnay.employee.new";

    public static final String PAYROLL_ADJUSTMENT_CHANNEL = "com.mycompnay.employee.payroll";

    public static void main(String... args) {


        /* Create you own private event bus. */
        EventManager privateEventBus = QBit.factory().createEventManager();


        final EventBusProxyCreator eventBusProxyCreator =
                QBit.factory().eventBusProxyCreator();

        final EmployeeEventManager employeeEventManager =
                eventBusProxyCreator.createProxy(privateEventBus, EmployeeEventManager.class);

        /*
        Create your EmployeeHiringService but this time pass the private event bus.
        Note you could easily use Spring or Guice for this wiring.
         */
        EmployeeHiringService employeeHiring = new EmployeeHiringService(employeeEventManager);



        /* Now create your other service POJOs which have no compile time dependencies on QBit. */
        PayrollService payroll = new PayrollService();
        BenefitsService benefits = new BenefitsService();
        VolunteerService volunteering = new VolunteerService();

        final QBitSystemManager systemManager = new QBitSystemManager();
        ServiceBuilder serviceBuilder = serviceBuilder()
                .setSystemManager(systemManager)
                .setInvokeDynamic(false);



        /* Create a service queue for this event bus. */
        Service privateEventBusService = serviceBuilder.build(privateEventBus);

        /** Employee hiring service. */
        Service employeeHiringService = serviceBuilder.build(employeeHiring);

        /** Payroll service */
        Service payrollService = serviceBuilder.build(payroll);

        /** Employee Benefits service. */
        Service employeeBenefitsService = serviceBuilder.build(benefits);

        /* Community outreach program. */
        Service volunteeringService = serviceBuilder.build(volunteering);


        /* Now wire in the event bus so it can fire events into the service queues. */
        privateEventBus.joinServices(
                payrollService,
                employeeBenefitsService,
                volunteeringService);

        systemManager.startAll();


        /** Now createWithWorkers the service proxy like before. */
        EmployeeHiringServiceClient employeeHiringServiceClientProxy =
                employeeHiringService.createProxy(EmployeeHiringServiceClient.class);

        /** Call the hireEmployee method which triggers the other events. */
        employeeHiringServiceClientProxy.hireEmployee(new Employee("Rick", 1));

        flushServiceProxy(employeeHiringServiceClientProxy);


        Thread thread = new Thread(
                () -> {
                    Sys.sleep(5_000);
                    puts("Calling shutdown\n\n");
                    systemManager.shutDown();
                }

        );
        thread.start();

        systemManager.waitForShutdown();

        puts("Shutdown complete from my sample");


    }

    interface EmployeeHiringServiceClient {
        void hireEmployee(final Employee employee);

    }


    interface EmployeeEventManager {

        @EventChannel(NEW_HIRE_CHANNEL)
        void sendNewEmployee(Employee employee);


        @EventChannel(PAYROLL_ADJUSTMENT_CHANNEL)
        void sendSalaryChangeEvent(Employee employee, int newSalary);

    }

    public static class Employee {
        final String firstName;
        final int employeeId;

        public Employee(String firstName, int employeeId) {
            this.firstName = firstName;
            this.employeeId = employeeId;
        }

        public String getFirstName() {
            return firstName;
        }

        public int getEmployeeId() {
            return employeeId;
        }

        @Override
        public String toString() {
            return "Employee{" +
                    "firstName='" + firstName + '\'' +
                    ", employeeId=" + employeeId +
                    '}';
        }
    }

    public static class EmployeeHiringService {

        final EmployeeEventManager eventManager;

        public EmployeeHiringService(final EmployeeEventManager employeeEventManager) {
            this.eventManager = employeeEventManager;
        }


        @QueueCallback(QueueCallbackType.EMPTY)
        private void noMoreRequests() {
            flushServiceProxy(eventManager);
        }


        @QueueCallback(QueueCallbackType.LIMIT)
        private void hitLimitOfRequests() {
            flushServiceProxy(eventManager);
        }


        public void hireEmployee(final Employee employee) {

            int salary = 100;
            System.out.printf("Hired employee %s\n", employee);

            //Does stuff to hire employee


            eventManager.sendNewEmployee(employee);
            eventManager.sendSalaryChangeEvent(employee, salary);


        }

    }

    public static class BenefitsService {

        @OnEvent(NEW_HIRE_CHANNEL)
        public void enroll(final Employee employee) {

            System.out.printf("Employee enrolled into benefits system employee %s %d\n",
                    employee.getFirstName(), employee.getEmployeeId());

        }

    }

    public static class VolunteerService {

        @OnEvent(NEW_HIRE_CHANNEL)
        public void invite(final Employee employee) {

            System.out.printf("Employee will be invited to the community outreach program %s %d\n",
                    employee.getFirstName(), employee.getEmployeeId());

        }

    }

    public static class PayrollService {

        @OnEvent(PAYROLL_ADJUSTMENT_CHANNEL)
        public void addEmployeeToPayroll(final Employee employee, int salary) {

            System.out.printf("Employee added to payroll  %s %d %d\n",
                    employee.getFirstName(), employee.getEmployeeId(), salary);

        }

    }
}