package com.megha.learningtracker.config;

import com.megha.learningtracker.entity.Phase;
import com.megha.learningtracker.entity.Topic;
import com.megha.learningtracker.entity.TopicStatus;
import com.megha.learningtracker.repository.PhaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Seeds the complete 30-phase Java Backend Mentorship curriculum on first run.
 * Only runs if the phases table is empty (idempotent).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final PhaseRepository phaseRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (phaseRepository.count() > 0) {
            log.info("✅ Curriculum already initialized ({} phases found)", phaseRepository.count());
            return;
        }

        log.info("🚀 Initializing curriculum — seeding 30 phases...");
        List<Phase> phases = buildCurriculum();
        phaseRepository.saveAll(phases);
        log.info("✅ Curriculum seeded: {} phases, {} topics",
                phases.size(),
                phases.stream().mapToInt(Phase::getTotalTopics).sum());
    }

    private List<Phase> buildCurriculum() {
        List<Phase> phases = new ArrayList<>();

        // ══════════════════════════════════════════════════
        // PHASE 0 — Developer Environment
        // ══════════════════════════════════════════════════
        phases.add(createPhase(0, "Developer Environment", "⚙️",
                "Master the tools and workflows every enterprise Java developer uses daily.",
                new String[][]{
                        {"IntelliJ IDEA Setup & Shortcuts", "Configure IntelliJ for enterprise Java development: plugins, shortcuts, debugging tools, and project import.", "45"},
                        {"Maven Fundamentals", "Understand Maven project structure, pom.xml, dependencies, repositories, and the build lifecycle.", "60"},
                        {"Maven Build Lifecycle & Plugins", "Deep dive into clean, compile, test, package, install, deploy phases and essential plugins.", "45"},
                        {"Git Version Control", "Master git init, add, commit, branch, merge, rebase, stash, log, and conflict resolution.", "60"},
                        {"Git Branching Strategies", "Learn GitFlow, trunk-based development, feature branches, and pull request workflows.", "45"},
                        {"Azure DevOps Basics", "Navigate Azure DevOps boards, repos, pipelines, and work item tracking.", "45"},
                        {"Terminal & Command Line", "Essential terminal commands for Java development: navigation, file operations, process management.", "30"},
                        {"Project Structure", "Understand standard Maven/Gradle project layouts, package conventions, and module organization.", "45"},
                        {"Running Spring Boot Applications", "Run applications from IDE and terminal, understand JAR packaging and startup process.", "30"},
                        {"Configuration Files (YAML & Properties)", "Master application.yml, application.properties, profile-specific configs, and property injection.", "45"},
                        {"Environment Variables & Secrets", "Manage environment-specific configuration, secrets, and twelve-factor app principles.", "30"},
                        {"Build & Dependency Management", "Resolve dependency conflicts, understand BOM, version management, and multi-module projects.", "45"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 1 — Java Fundamentals
        // ══════════════════════════════════════════════════
        phases.add(createPhase(1, "Java Fundamentals", "☕",
                "Build a rock-solid foundation in core Java syntax, types, and program structure.",
                new String[][]{
                        {"Variables & Data Types", "Primitive types (int, long, double, boolean, char), reference types, type casting, and literals.", "60"},
                        {"Operators", "Arithmetic, relational, logical, bitwise, ternary, and assignment operators with precedence rules.", "45"},
                        {"Methods", "Method declaration, parameters, return types, overloading, varargs, and call stack mechanics.", "60"},
                        {"Control Statements", "if-else, switch, for, while, do-while, break, continue, and nested control flow.", "60"},
                        {"Arrays", "Single and multi-dimensional arrays, initialization, iteration, Arrays utility class, and common algorithms.", "45"},
                        {"Strings & String Methods", "String immutability, String pool, StringBuilder, StringBuffer, and essential string operations.", "60"},
                        {"String Manipulation Advanced", "Regular expressions, String.format(), text blocks, and performance implications.", "45"},
                        {"Packages & Imports", "Package naming conventions, access modifiers, static imports, and classpath management.", "30"},
                        {"Classes & Objects", "Class anatomy, constructors, this keyword, object creation, and memory allocation.", "60"},
                        {"Static Members & Initialization", "Static variables, methods, blocks, instance initializers, and class loading order.", "45"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 2 — Object-Oriented Programming
        // ══════════════════════════════════════════════════
        phases.add(createPhase(2, "Object-Oriented Programming", "🏗️",
                "Master the four pillars of OOP and how they enable enterprise software design.",
                new String[][]{
                        {"Encapsulation", "Access modifiers, getters/setters, data hiding, and immutable objects.", "60"},
                        {"Inheritance", "extends keyword, super keyword, constructor chaining, method overriding, and IS-A relationships.", "60"},
                        {"Polymorphism", "Compile-time (overloading) vs runtime (overriding) polymorphism, upcasting, downcasting, instanceof.", "60"},
                        {"Abstraction", "Abstract classes vs interfaces, when to use each, and designing abstractions.", "60"},
                        {"Interfaces", "Default methods, static methods, functional interfaces, multiple inheritance via interfaces.", "60"},
                        {"Abstract Classes", "Template Method pattern, partial implementations, and abstract class hierarchies.", "45"},
                        {"Composition over Inheritance", "HAS-A relationships, delegation, and why composition is preferred in enterprise code.", "45"},
                        {"Association, Aggregation & Composition", "Object relationships, lifecycle dependencies, and UML representation.", "45"},
                        {"SOLID Principles Introduction", "Single Responsibility, Open-Closed, Liskov Substitution, Interface Segregation, Dependency Inversion.", "60"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 3 — Collections Framework
        // ══════════════════════════════════════════════════
        phases.add(createPhase(3, "Collections Framework", "📦",
                "Master Java's data structure library — the backbone of every enterprise application.",
                new String[][]{
                        {"List: ArrayList & LinkedList", "Internal structure, performance characteristics, when to use each, and common operations.", "60"},
                        {"Set: HashSet, LinkedHashSet & TreeSet", "Hashing, uniqueness, ordering guarantees, and equals/hashCode contract.", "60"},
                        {"Map: HashMap, LinkedHashMap & TreeMap", "Hash table internals, collision handling, load factor, and tree-based maps.", "60"},
                        {"Queue, Deque & PriorityQueue", "FIFO/LIFO structures, ArrayDeque, and priority-based processing.", "45"},
                        {"Iterator & Iterable", "Iterator pattern, enhanced for-loop, ListIterator, and fail-fast behavior.", "30"},
                        {"Comparable Interface", "Natural ordering, compareTo() contract, and sorting collections.", "30"},
                        {"Comparator Interface", "Custom sorting, chained comparators, and Comparator.comparing().", "30"},
                        {"Collections Performance & Best Practices", "Time complexity comparison, choosing the right collection, and thread-safe alternatives.", "45"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 4 — Exception Handling
        // ══════════════════════════════════════════════════
        phases.add(createPhase(4, "Exception Handling", "🛡️",
                "Learn to write robust error handling code that makes debugging production issues easier.",
                new String[][]{
                        {"Checked Exceptions", "Compile-time exceptions, throws declaration, IOException, SQLException, and handling strategies.", "45"},
                        {"Unchecked Exceptions", "RuntimeExceptions, NullPointerException, IllegalArgumentException, and defensive programming.", "45"},
                        {"Custom Exceptions", "Creating domain-specific exceptions, exception hierarchies, and error codes.", "45"},
                        {"Exception Propagation", "Call stack unwinding, try-catch-finally, try-with-resources, and multi-catch.", "45"},
                        {"Exception Handling Best Practices", "Logging exceptions, never swallowing exceptions, exception translation, and enterprise patterns.", "45"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 5 — Java 8+ Features
        // ══════════════════════════════════════════════════
        phases.add(createPhase(5, "Java 8+ Features", "✨",
                "Modern Java features that are used extensively in enterprise Spring Boot code.",
                new String[][]{
                        {"Lambda Expressions", "Syntax, functional interfaces, variable capture, effectively final, and type inference.", "60"},
                        {"Streams API", "Stream creation, intermediate operations (map, filter, flatMap), terminal operations (collect, reduce, forEach).", "90"},
                        {"Streams Advanced", "Parallel streams, custom collectors, groupingBy, partitioningBy, and performance considerations.", "60"},
                        {"Optional Class", "Creating Optionals, map, flatMap, orElse, orElseGet, orElseThrow, and avoiding null checks.", "45"},
                        {"Method References", "Static, instance, constructor references, and when to use them over lambdas.", "30"},
                        {"Functional Interfaces", "Predicate, Function, Consumer, Supplier, BiFunction, and composing functions.", "45"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 6 — Generics
        // ══════════════════════════════════════════════════
        phases.add(createPhase(6, "Generics", "🔷",
                "Type-safe programming that powers Spring's dependency injection and collection APIs.",
                new String[][]{
                        {"Generic Classes & Interfaces", "Type parameters, generic class design, and type safety benefits.", "45"},
                        {"Generic Methods", "Method-level type parameters, type inference, and bounded type parameters.", "45"},
                        {"Bounded Types", "Upper bounds (extends), lower bounds (super), and multiple bounds.", "45"},
                        {"Wildcards", "Unbounded (?), upper bounded (? extends), lower bounded (? super), and PECS principle.", "45"},
                        {"Type Erasure", "How generics work at runtime, bridge methods, and limitations.", "30"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 7 — Multithreading & Concurrency
        // ══════════════════════════════════════════════════
        phases.add(createPhase(7, "Multithreading & Concurrency", "🔄",
                "Understand concurrent programming for building responsive, high-throughput backend systems.",
                new String[][]{
                        {"Thread Creation & Lifecycle", "Thread class, Runnable, thread states, start(), sleep(), join(), and daemon threads.", "60"},
                        {"Synchronization", "synchronized keyword, locks, monitors, deadlocks, and thread safety.", "60"},
                        {"Executor Framework", "ThreadPool, ExecutorService, ScheduledExecutorService, and thread pool sizing.", "60"},
                        {"CompletableFuture", "Async computation, thenApply, thenCompose, allOf, anyOf, and exception handling.", "60"},
                        {"Concurrent Collections", "ConcurrentHashMap, CopyOnWriteArrayList, BlockingQueue, and atomic variables.", "45"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 8 — JVM Internals
        // ══════════════════════════════════════════════════
        phases.add(createPhase(8, "JVM Internals", "🧠",
                "Understand the Java Virtual Machine for performance tuning and production debugging.",
                new String[][]{
                        {"JVM Architecture", "ClassLoader, Runtime Data Areas, Execution Engine, and JVM specification overview.", "60"},
                        {"Memory Model: Heap & Stack", "Young/Old generation, stack frames, method area, and memory allocation.", "60"},
                        {"Garbage Collection", "GC algorithms (Serial, Parallel, G1, ZGC), GC roots, and GC tuning.", "60"},
                        {"JIT Compilation", "Interpreter vs compiler, tiered compilation, and hot code optimization.", "30"},
                        {"Class Loading", "Bootstrap, extension, application classloaders, and custom classloading.", "30"},
                        {"JVM Performance Tuning", "JVM flags, heap sizing, GC logs, thread dumps, and heap dumps.", "60"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 9 — SQL & Databases
        // ══════════════════════════════════════════════════
        phases.add(createPhase(9, "SQL & Databases", "🗄️",
                "Master relational databases — every backend developer's essential skill.",
                new String[][]{
                        {"Database Design & ER Modeling", "Tables, columns, primary keys, foreign keys, and ER diagrams.", "60"},
                        {"CRUD Operations", "INSERT, SELECT, UPDATE, DELETE with conditions and best practices.", "45"},
                        {"Joins", "INNER, LEFT, RIGHT, FULL, CROSS joins, self-joins, and join optimization.", "60"},
                        {"Indexes", "B-tree indexes, composite indexes, covering indexes, and index strategy.", "45"},
                        {"Transactions & ACID", "BEGIN, COMMIT, ROLLBACK, isolation levels, and transaction management.", "60"},
                        {"Normalization", "1NF, 2NF, 3NF, BCNF, denormalization, and when to break rules.", "45"},
                        {"Query Optimization", "EXPLAIN plans, slow query analysis, N+1 problem, and optimization techniques.", "60"},
                        {"Advanced SQL", "Subqueries, CTEs, window functions, aggregations, and stored procedures.", "60"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 10 — HTTP & REST APIs
        // ══════════════════════════════════════════════════
        phases.add(createPhase(10, "HTTP & REST APIs", "🌐",
                "Understand the protocol that powers all web communication.",
                new String[][]{
                        {"HTTP Protocol Fundamentals", "Request/response cycle, TCP connections, HTTP/1.1 vs HTTP/2, and HTTPS.", "60"},
                        {"HTTP Methods", "GET, POST, PUT, PATCH, DELETE — semantics, idempotency, and safe methods.", "45"},
                        {"HTTP Status Codes", "1xx–5xx status codes, when to use each, and error handling conventions.", "30"},
                        {"Headers & Content Negotiation", "Request/response headers, content types, Accept, Authorization, and caching headers.", "45"},
                        {"Cookies & Sessions", "Session management, cookie attributes, CSRF, and stateless authentication.", "45"},
                        {"JSON Format", "JSON syntax, data types, nested objects, arrays, and JSON Schema.", "30"},
                        {"Serialization & Deserialization", "Jackson library, @JsonProperty, @JsonIgnore, custom serializers, and date handling.", "45"},
                        {"REST Principles", "RESTful constraints, resource naming, HATEOAS, versioning, and Richardson Maturity Model.", "60"},
                        {"API Design Best Practices", "Pagination, filtering, sorting, error formats, and API documentation.", "45"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 11 — Spring Core
        // ══════════════════════════════════════════════════
        phases.add(createPhase(11, "Spring Core", "🌱",
                "The foundation of the entire Spring ecosystem — IoC and dependency injection.",
                new String[][]{
                        {"IoC Container", "Inversion of Control principle, BeanFactory, ApplicationContext, and container lifecycle.", "60"},
                        {"Dependency Injection", "Constructor injection, setter injection, field injection, and best practices.", "60"},
                        {"Spring Beans", "@Component, @Service, @Repository, @Controller, bean scopes (singleton, prototype).", "60"},
                        {"Bean Lifecycle", "Init, destroy callbacks, @PostConstruct, @PreDestroy, and BeanPostProcessor.", "45"},
                        {"Component Scanning", "@ComponentScan, base packages, filtering, and auto-detection.", "30"},
                        {"Java-based Configuration", "@Configuration, @Bean, @Import, @PropertySource, and configuration composition.", "45"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 12 — Spring Boot
        // ══════════════════════════════════════════════════
        phases.add(createPhase(12, "Spring Boot", "🚀",
                "Spring Boot's auto-configuration magic that makes enterprise development fast.",
                new String[][]{
                        {"Auto Configuration", "@EnableAutoConfiguration, spring.factories, conditional annotations, and how it works.", "60"},
                        {"Spring Boot Starters", "Starter POMs, transitive dependencies, and creating custom starters.", "45"},
                        {"Properties & YAML Configuration", "@Value, @ConfigurationProperties, relaxed binding, and property precedence.", "45"},
                        {"Profiles", "@Profile, profile-specific configs, activating profiles, and multi-environment setup.", "45"},
                        {"Configuration Classes", "@Configuration deep dive, conditional beans, and configuration best practices.", "30"},
                        {"Spring Boot Actuator", "Health checks, metrics, info endpoints, custom actuator endpoints, and production monitoring.", "60"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 13 — Spring MVC
        // ══════════════════════════════════════════════════
        phases.add(createPhase(13, "Spring MVC", "🎯",
                "Build REST APIs with Spring MVC — the controller layer of enterprise applications.",
                new String[][]{
                        {"@Controller & @RestController", "MVC pattern, controller responsibilities, @ResponseBody, and REST vs MVC controllers.", "45"},
                        {"Request Mapping", "@RequestMapping, @GetMapping, @PostMapping, path variables, query parameters, and request body.", "60"},
                        {"DTOs & Data Binding", "Request/response DTOs, model mapping, @RequestBody, @ResponseBody, and conversion.", "45"},
                        {"Validation", "@Valid, @Validated, Bean Validation annotations, and BindingResult.", "45"},
                        {"ResponseEntity", "Status codes, headers, body, builder pattern, and response construction.", "30"},
                        {"Request Flow Architecture", "DispatcherServlet, HandlerMapping, HandlerAdapter, ViewResolver, and interceptors.", "60"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 14 — Spring Validation
        // ══════════════════════════════════════════════════
        phases.add(createPhase(14, "Spring Validation", "✅",
                "Input validation — the first line of defense in enterprise APIs.",
                new String[][]{
                        {"Bean Validation (JSR 380)", "@NotNull, @NotBlank, @Size, @Email, @Min, @Max, @Pattern, and constraint composition.", "45"},
                        {"Custom Validators", "Creating custom constraint annotations and ConstraintValidator implementations.", "45"},
                        {"Validation Groups", "Group-based validation for create vs update scenarios.", "30"},
                        {"Global Validation Handling", "MethodArgumentNotValidException handling and user-friendly error messages.", "30"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 15 — Spring Exception Handling
        // ══════════════════════════════════════════════════
        phases.add(createPhase(15, "Spring Exception Handling", "🚨",
                "Centralized exception handling for clean, consistent API error responses.",
                new String[][]{
                        {"@ControllerAdvice", "Global exception handling, @ExceptionHandler, and advice ordering.", "45"},
                        {"Exception Handlers", "Handling specific exceptions, exception hierarchies, and ResponseStatusException.", "45"},
                        {"Error Response Structure", "Standardized error format, error codes, field-level errors, and RFC 7807 Problem Details.", "45"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 16 — Spring Data JPA
        // ══════════════════════════════════════════════════
        phases.add(createPhase(16, "Spring Data JPA", "💾",
                "Database access made elegant — JPA repositories, entities, and Hibernate ORM.",
                new String[][]{
                        {"JPA Repositories", "JpaRepository, CrudRepository, query methods, and derived queries.", "60"},
                        {"Entity Mapping", "@Entity, @Table, @Column, @Id, @GeneratedValue, and naming strategies.", "60"},
                        {"Relationships", "@OneToMany, @ManyToOne, @OneToOne, @ManyToMany, cascade, and fetch types.", "60"},
                        {"JPQL & Native Queries", "@Query annotation, named parameters, native SQL, and projections.", "45"},
                        {"Lazy vs Eager Loading", "FetchType.LAZY, FetchType.EAGER, N+1 problem, and @EntityGraph.", "45"},
                        {"Transaction Management", "@Transactional, propagation, isolation levels, and rollback rules.", "60"},
                        {"Hibernate Internals", "First-level cache, second-level cache, dirty checking, and flush modes.", "45"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 17 — Spring Security
        // ══════════════════════════════════════════════════
        phases.add(createPhase(17, "Spring Security", "🔐",
                "Secure your APIs with authentication and authorization.",
                new String[][]{
                        {"Authentication Flow", "AuthenticationManager, AuthenticationProvider, UserDetailsService, and PasswordEncoder.", "60"},
                        {"Authorization", "Roles, authorities, @PreAuthorize, @Secured, method-level security.", "60"},
                        {"JWT Tokens", "JWT structure, token generation, validation, refresh tokens, and stateless auth.", "60"},
                        {"Security Filters", "Filter chain, custom filters, OncePerRequestFilter, and filter ordering.", "60"},
                        {"Security Context", "SecurityContextHolder, Authentication object, Principal, and thread-local storage.", "45"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 18 — Testing
        // ══════════════════════════════════════════════════
        phases.add(createPhase(18, "Testing", "🧪",
                "Write tests that give you confidence to refactor and deploy enterprise code.",
                new String[][]{
                        {"JUnit 5", "Test lifecycle, assertions, parameterized tests, nested tests, and test organization.", "60"},
                        {"Mockito", "Mocking, stubbing, verification, argument captors, and spy objects.", "60"},
                        {"MockMvc", "Testing REST controllers, request builders, result matchers, and JSON assertions.", "60"},
                        {"Integration Testing", "@SpringBootTest, test profiles, test databases, and slice tests (@WebMvcTest, @DataJpaTest).", "60"},
                        {"TestContainers", "Docker-based integration tests with real databases and external services.", "45"},
                        {"Code Coverage", "JaCoCo, coverage metrics, meaningful coverage, and testing strategies.", "30"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 19 — Microservices Architecture
        // ══════════════════════════════════════════════════
        phases.add(createPhase(19, "Microservices Architecture", "🏛️",
                "Design and build distributed systems with independently deployable services.",
                new String[][]{
                        {"Microservices Principles", "Bounded contexts, single responsibility, independent deployment, and trade-offs.", "60"},
                        {"Service Communication", "REST, gRPC, async messaging, and choosing communication patterns.", "45"},
                        {"API Gateway", "Spring Cloud Gateway, routing, rate limiting, and request transformation.", "60"},
                        {"Feign Client", "Declarative HTTP clients, @FeignClient, fallbacks, and circuit breaker integration.", "45"},
                        {"Config Server", "Centralized configuration, Spring Cloud Config, and dynamic refresh.", "45"},
                        {"Service Discovery", "Eureka, Consul, service registration, and client-side load balancing.", "45"},
                        {"Resilience Patterns", "Circuit breaker (Resilience4j), retry, bulkhead, rate limiter, and fallbacks.", "60"},
                        {"Distributed Transactions", "Saga pattern, eventual consistency, compensation, and outbox pattern.", "60"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 20 — Messaging
        // ══════════════════════════════════════════════════
        phases.add(createPhase(20, "Messaging", "📨",
                "Asynchronous communication between services using message brokers.",
                new String[][]{
                        {"Apache Kafka", "Topics, partitions, producers, consumers, consumer groups, and exactly-once semantics.", "90"},
                        {"RabbitMQ", "Exchanges, queues, bindings, routing keys, and acknowledgment modes.", "60"},
                        {"Event-Driven Architecture", "Event sourcing, CQRS, event schemas, and event-driven design patterns.", "60"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 21 — Caching
        // ══════════════════════════════════════════════════
        phases.add(createPhase(21, "Caching", "⚡",
                "Speed up your application with caching strategies.",
                new String[][]{
                        {"Redis", "Redis data types, Spring Data Redis, RedisTemplate, and cache configuration.", "60"},
                        {"Caching Strategies", "Cache-aside, write-through, write-behind, TTL, eviction policies, and cache invalidation.", "60"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 22 — Docker
        // ══════════════════════════════════════════════════
        phases.add(createPhase(22, "Docker", "🐳",
                "Containerize your applications for consistent deployments.",
                new String[][]{
                        {"Docker Images", "Dockerfile, multi-stage builds, layers, caching, and image optimization.", "60"},
                        {"Containers", "Container lifecycle, port mapping, environment variables, and container networking.", "60"},
                        {"Volumes", "Data persistence, volume mounts, bind mounts, and named volumes.", "30"},
                        {"Docker Networking", "Bridge, host, overlay networks, and container-to-container communication.", "30"},
                        {"Docker Compose", "Multi-container apps, docker-compose.yml, service dependencies, and local development.", "60"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 23 — Kubernetes
        // ══════════════════════════════════════════════════
        phases.add(createPhase(23, "Kubernetes", "☸️",
                "Orchestrate containers at scale in production environments.",
                new String[][]{
                        {"Kubernetes Architecture", "Control plane, worker nodes, kubelet, kube-proxy, and etcd.", "60"},
                        {"Pods", "Pod lifecycle, multi-container pods, init containers, and resource limits.", "45"},
                        {"Deployments", "Rolling updates, rollbacks, replicas, deployment strategies, and health checks.", "45"},
                        {"Services", "ClusterIP, NodePort, LoadBalancer, and service discovery.", "45"},
                        {"Ingress", "Ingress controllers, routing rules, TLS termination, and path-based routing.", "45"},
                        {"ConfigMaps & Secrets", "External configuration, secret management, and volume mounts.", "30"},
                        {"Argo CD", "GitOps, continuous delivery, application definitions, and sync strategies.", "45"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 24 — Azure DevOps
        // ══════════════════════════════════════════════════
        phases.add(createPhase(24, "Azure DevOps", "☁️",
                "Enterprise CI/CD and project management with Azure DevOps.",
                new String[][]{
                        {"Boards & Work Items", "Epics, features, user stories, tasks, sprint planning, and kanban boards.", "45"},
                        {"Azure Repos", "Git repositories, branch policies, pull requests, and code reviews.", "45"},
                        {"Pipelines", "Build pipelines, YAML pipeline syntax, triggers, stages, and templates.", "60"},
                        {"Releases", "Release pipelines, approvals, environments, and deployment gates.", "45"},
                        {"Artifacts", "Package feeds, Maven/npm artifact publishing, and versioning.", "30"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 25 — Production Engineering
        // ══════════════════════════════════════════════════
        phases.add(createPhase(25, "Production Engineering", "🔧",
                "Keep applications healthy in production — logging, monitoring, and debugging.",
                new String[][]{
                        {"Logging (SLF4J & Logback)", "Log levels, structured logging, MDC, log patterns, and log aggregation.", "60"},
                        {"Monitoring", "Micrometer, Prometheus, Grafana dashboards, and custom metrics.", "60"},
                        {"Distributed Tracing", "OpenTelemetry, trace IDs, span IDs, and request correlation.", "45"},
                        {"Metrics & Alerting", "Business metrics, SLIs, SLOs, SLAs, and alerting strategies.", "45"},
                        {"Performance Tuning", "Profiling, thread pool tuning, connection pool sizing, and bottleneck identification.", "60"},
                        {"Production Debugging", "Remote debugging, heap dumps, thread dumps, and JFR.", "60"},
                        {"Incident Investigation", "Root cause analysis, post-mortems, runbooks, and on-call practices.", "45"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 26 — Design Patterns
        // ══════════════════════════════════════════════════
        phases.add(createPhase(26, "Design Patterns", "🎨",
                "Time-tested solutions to common software design problems.",
                new String[][]{
                        {"Creational Patterns", "Singleton, Factory, Abstract Factory, Builder, and Prototype patterns.", "60"},
                        {"Structural Patterns", "Adapter, Decorator, Facade, Proxy, Bridge, and Composite patterns.", "60"},
                        {"Behavioral Patterns", "Strategy, Observer, Template Method, Command, Chain of Responsibility, and State.", "60"},
                        {"Enterprise Patterns", "DTO, Repository, Service Layer, Unit of Work, and Domain-Driven Design patterns.", "60"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 27 — Low Level Design
        // ══════════════════════════════════════════════════
        phases.add(createPhase(27, "Low Level Design", "📐",
                "Design classes, interfaces, and modules for maintainability and extensibility.",
                new String[][]{
                        {"SOLID Principles Deep Dive", "Apply each SOLID principle with real enterprise examples and code.", "60"},
                        {"Design Principles", "DRY, KISS, YAGNI, Law of Demeter, and Composition Root.", "45"},
                        {"UML Diagrams", "Class diagrams, sequence diagrams, activity diagrams, and use case diagrams.", "45"},
                        {"LLD Practice: Parking Lot", "Design a parking lot system — classes, interfaces, and interactions.", "60"},
                        {"LLD Practice: Library System", "Design a library management system with OOP principles.", "60"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 28 — High Level System Design
        // ══════════════════════════════════════════════════
        phases.add(createPhase(28, "High Level System Design", "🏗️",
                "Design scalable, distributed systems that handle millions of requests.",
                new String[][]{
                        {"Scalability", "Horizontal vs vertical scaling, stateless services, and autoscaling.", "60"},
                        {"Load Balancing", "L4 vs L7 load balancers, algorithms, health checks, and sticky sessions.", "45"},
                        {"Caching Architectures", "CDN, application cache, database cache, cache coherence, and cache stampede.", "45"},
                        {"Database Scaling", "Sharding, replication, read replicas, partitioning strategies, and connection pooling.", "60"},
                        {"CAP Theorem", "Consistency, Availability, Partition Tolerance, and real-world trade-offs.", "45"},
                        {"Distributed Systems Design", "Design URL shortener, rate limiter, notification system, and chat system.", "90"}
                }
        ));

        // ══════════════════════════════════════════════════
        // PHASE 29 — Modern AI Backend
        // ══════════════════════════════════════════════════
        phases.add(createPhase(29, "Modern AI Backend", "🤖",
                "Build AI-powered Java backend applications with LLMs, RAG, and agents.",
                new String[][]{
                        {"Spring AI Framework", "Spring AI overview, model clients, prompt templates, and output parsers.", "60"},
                        {"LangChain4j", "LangChain4j architecture, chains, agents, and integration with Spring Boot.", "60"},
                        {"LLM API Integration", "OpenAI, Gemini, Anthropic APIs — chat completions, streaming, and configuration.", "60"},
                        {"Model Context Protocol (MCP)", "MCP specification, tool servers, resource management, and integration.", "45"},
                        {"AI Agents", "Agent architecture, planning, tool use, and multi-step reasoning.", "60"},
                        {"Agentic AI Patterns", "ReAct, chain-of-thought, self-reflection, and orchestration patterns.", "45"},
                        {"RAG: Retrieval Augmented Generation", "Document loading, chunking, embedding, retrieval, and generation pipeline.", "90"},
                        {"Embeddings", "Text embeddings, embedding models, similarity search, and dimensionality.", "45"},
                        {"Vector Databases", "Pinecone, Milvus, pgvector, ChromaDB — indexing and similarity queries.", "60"},
                        {"Tool Calling", "Function calling, tool definitions, parameter schemas, and tool execution.", "45"},
                        {"AI Memory Management", "Conversation memory, sliding window, summary memory, and persistent memory.", "45"},
                        {"AI Workflow Orchestration", "Multi-agent systems, workflow graphs, and production AI architectures.", "60"}
                }
        ));

        return phases;
    }

    /**
     * Helper method to create a Phase with its topics.
     */
    private Phase createPhase(int phaseNumber, String title, String icon,
                              String description, String[][] topicsData) {
        Phase phase = Phase.builder()
                .phaseNumber(phaseNumber)
                .title(title)
                .icon(icon)
                .description(description)
                .totalTopics(topicsData.length)
                .completedTopics(0)
                .status(TopicStatus.NOT_STARTED)
                .topics(new ArrayList<>())
                .build();

        for (int i = 0; i < topicsData.length; i++) {
            Topic topic = Topic.builder()
                    .phase(phase)
                    .title(topicsData[i][0])
                    .description(topicsData[i][1])
                    .estimatedMinutes(Integer.parseInt(topicsData[i][2]))
                    .status(TopicStatus.NOT_STARTED)
                    .xpReward(10)
                    .sortOrder(i)
                    .build();
            phase.getTopics().add(topic);
        }

        return phase;
    }
}
