//package org.camunda.bpm.engine.impl.cfg;
//
//import org.apache.ibatis.builder.xml.XMLConfigBuilder;
//import org.apache.ibatis.datasource.pooled.PooledDataSource;
//import org.apache.ibatis.mapping.Environment;
//import org.apache.ibatis.session.Configuration;
//import org.apache.ibatis.session.ExecutorType;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
//import org.apache.ibatis.transaction.TransactionFactory;
//import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
//import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
//import org.camunda.bpm.dmn.engine.DmnEngine;
//import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
//import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngineConfiguration;
//import org.camunda.bpm.engine.*;
//import org.camunda.bpm.engine.authorization.Permission;
//import org.camunda.bpm.engine.authorization.Permissions;
//import org.camunda.bpm.engine.impl.*;
//import org.camunda.bpm.engine.impl.application.ProcessApplicationManager;
//import org.camunda.bpm.engine.impl.batch.BatchJobHandler;
//import org.camunda.bpm.engine.impl.batch.BatchMonitorJobHandler;
//import org.camunda.bpm.engine.impl.batch.BatchSeedJobHandler;
//import org.camunda.bpm.engine.impl.batch.deletion.DeleteHistoricProcessInstancesJobHandler;
//import org.camunda.bpm.engine.impl.batch.deletion.DeleteProcessInstancesJobHandler;
//import org.camunda.bpm.engine.impl.batch.externaltask.SetExternalTaskRetriesJobHandler;
//import org.camunda.bpm.engine.impl.batch.job.SetJobRetriesJobHandler;
//import org.camunda.bpm.engine.impl.batch.update.UpdateProcessInstancesSuspendStateJobHandler;
//import org.camunda.bpm.engine.impl.bpmn.behavior.ExternalTaskActivityBehavior;
//import org.camunda.bpm.engine.impl.bpmn.deployer.BpmnDeployer;
//import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
//import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParser;
//import org.camunda.bpm.engine.impl.bpmn.parser.DefaultFailedJobParseListener;
//import org.camunda.bpm.engine.impl.calendar.*;
//import org.camunda.bpm.engine.impl.cfg.auth.AuthorizationCommandChecker;
//import org.camunda.bpm.engine.impl.cfg.auth.DefaultAuthorizationProvider;
//import org.camunda.bpm.engine.impl.cfg.auth.ResourceAuthorizationProvider;
//import org.camunda.bpm.engine.impl.cfg.multitenancy.TenantCommandChecker;
//import org.camunda.bpm.engine.impl.cfg.multitenancy.TenantIdProvider;
//import org.camunda.bpm.engine.impl.cfg.standalone.StandaloneTransactionContextFactory;
//import org.camunda.bpm.engine.impl.cmmn.CaseServiceImpl;
//import org.camunda.bpm.engine.impl.cmmn.deployer.CmmnDeployer;
//import org.camunda.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionManager;
//import org.camunda.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionManager;
//import org.camunda.bpm.engine.impl.cmmn.entity.runtime.CaseSentryPartManager;
//import org.camunda.bpm.engine.impl.cmmn.handler.DefaultCmmnElementHandlerRegistry;
//import org.camunda.bpm.engine.impl.cmmn.transformer.CmmnTransformFactory;
//import org.camunda.bpm.engine.impl.cmmn.transformer.CmmnTransformListener;
//import org.camunda.bpm.engine.impl.cmmn.transformer.CmmnTransformer;
//import org.camunda.bpm.engine.impl.cmmn.transformer.DefaultCmmnTransformFactory;
//import org.camunda.bpm.engine.impl.db.DbIdGenerator;
//import org.camunda.bpm.engine.impl.db.entitymanager.DbEntityManagerFactory;
//import org.camunda.bpm.engine.impl.db.entitymanager.cache.DbEntityCacheKeyMapping;
//import org.camunda.bpm.engine.impl.db.sql.DbSqlPersistenceProviderFactory;
//import org.camunda.bpm.engine.impl.db.sql.DbSqlSessionFactory;
//import org.camunda.bpm.engine.impl.delegate.DefaultDelegateInterceptor;
//import org.camunda.bpm.engine.impl.digest.*;
//import org.camunda.bpm.engine.impl.dmn.batch.DeleteHistoricDecisionInstancesJobHandler;
//import org.camunda.bpm.engine.impl.dmn.configuration.DmnEngineConfigurationBuilder;
//import org.camunda.bpm.engine.impl.dmn.deployer.DecisionDefinitionDeployer;
//import org.camunda.bpm.engine.impl.dmn.deployer.DecisionRequirementsDefinitionDeployer;
//import org.camunda.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionManager;
//import org.camunda.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionManager;
//import org.camunda.bpm.engine.impl.el.CommandContextFunctionMapper;
//import org.camunda.bpm.engine.impl.el.DateTimeFunctionMapper;
//import org.camunda.bpm.engine.impl.el.ExpressionManager;
//import org.camunda.bpm.engine.impl.event.*;
//import org.camunda.bpm.engine.impl.externaltask.DefaultExternalTaskPriorityProvider;
//import org.camunda.bpm.engine.impl.form.engine.FormEngine;
//import org.camunda.bpm.engine.impl.form.engine.HtmlFormEngine;
//import org.camunda.bpm.engine.impl.form.engine.JuelFormEngine;
//import org.camunda.bpm.engine.impl.form.type.*;
//import org.camunda.bpm.engine.impl.form.validator.*;
//import org.camunda.bpm.engine.impl.history.DefaultHistoryRemovalTimeProvider;
//import org.camunda.bpm.engine.impl.history.HistoryLevel;
//import org.camunda.bpm.engine.impl.history.HistoryRemovalTimeProvider;
//import org.camunda.bpm.engine.impl.history.event.HistoricDecisionInstanceManager;
//import org.camunda.bpm.engine.impl.history.handler.DbHistoryEventHandler;
//import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler;
//import org.camunda.bpm.engine.impl.history.parser.HistoryParseListener;
//import org.camunda.bpm.engine.impl.history.producer.*;
//import org.camunda.bpm.engine.impl.history.transformer.CmmnHistoryTransformListener;
//import org.camunda.bpm.engine.impl.identity.ReadOnlyIdentityProvider;
//import org.camunda.bpm.engine.impl.identity.WritableIdentityProvider;
//import org.camunda.bpm.engine.impl.identity.db.DbIdentityServiceProvider;
//import org.camunda.bpm.engine.impl.incident.DefaultIncidentHandler;
//import org.camunda.bpm.engine.impl.incident.IncidentHandler;
//import org.camunda.bpm.engine.impl.interceptor.*;
//import org.camunda.bpm.engine.impl.jobexecutor.*;
//import org.camunda.bpm.engine.impl.jobexecutor.historycleanup.BatchWindowManager;
//import org.camunda.bpm.engine.impl.jobexecutor.historycleanup.DefaultBatchWindowManager;
//import org.camunda.bpm.engine.impl.jobexecutor.historycleanup.HistoryCleanupHelper;
//import org.camunda.bpm.engine.impl.jobexecutor.historycleanup.HistoryCleanupJobHandler;
//import org.camunda.bpm.engine.impl.metrics.MetricsRegistry;
//import org.camunda.bpm.engine.impl.metrics.MetricsReporterIdProvider;
//import org.camunda.bpm.engine.impl.metrics.SimpleIpBasedProvider;
//import org.camunda.bpm.engine.impl.metrics.parser.MetricsBpmnParseListener;
//import org.camunda.bpm.engine.impl.metrics.parser.MetricsCmmnTransformListener;
//import org.camunda.bpm.engine.impl.metrics.reporter.DbMetricsReporter;
//import org.camunda.bpm.engine.impl.migration.DefaultMigrationActivityMatcher;
//import org.camunda.bpm.engine.impl.migration.DefaultMigrationInstructionGenerator;
//import org.camunda.bpm.engine.impl.migration.MigrationActivityMatcher;
//import org.camunda.bpm.engine.impl.migration.MigrationInstructionGenerator;
//import org.camunda.bpm.engine.impl.migration.batch.MigrationBatchJobHandler;
//import org.camunda.bpm.engine.impl.migration.validation.activity.MigrationActivityValidator;
//import org.camunda.bpm.engine.impl.migration.validation.activity.NoCompensationHandlerActivityValidator;
//import org.camunda.bpm.engine.impl.migration.validation.activity.SupportedActivityValidator;
//import org.camunda.bpm.engine.impl.migration.validation.activity.SupportedPassiveEventTriggerActivityValidator;
//import org.camunda.bpm.engine.impl.migration.validation.instance.*;
//import org.camunda.bpm.engine.impl.migration.validation.instruction.*;
//import org.camunda.bpm.engine.impl.optimize.OptimizeManager;
//import org.camunda.bpm.engine.impl.persistence.GenericManagerFactory;
//import org.camunda.bpm.engine.impl.persistence.deploy.Deployer;
//import org.camunda.bpm.engine.impl.persistence.deploy.cache.CacheFactory;
//import org.camunda.bpm.engine.impl.persistence.deploy.cache.DefaultCacheFactory;
//import org.camunda.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
//import org.camunda.bpm.engine.impl.persistence.entity.*;
//import org.camunda.bpm.engine.impl.runtime.ConditionHandler;
//import org.camunda.bpm.engine.impl.runtime.CorrelationHandler;
//import org.camunda.bpm.engine.impl.runtime.DefaultConditionHandler;
//import org.camunda.bpm.engine.impl.runtime.DefaultCorrelationHandler;
//import org.camunda.bpm.engine.impl.scripting.ScriptFactory;
//import org.camunda.bpm.engine.impl.scripting.engine.*;
//import org.camunda.bpm.engine.impl.scripting.env.ScriptEnvResolver;
//import org.camunda.bpm.engine.impl.scripting.env.ScriptingEnvironment;
//import org.camunda.bpm.engine.impl.util.EnsureUtil;
//import org.camunda.bpm.engine.impl.util.IoUtil;
//import org.camunda.bpm.engine.impl.util.ParseUtil;
//import org.camunda.bpm.engine.impl.util.ReflectUtil;
//import org.camunda.bpm.engine.impl.variable.ValueTypeResolverImpl;
//import org.camunda.bpm.engine.impl.variable.serializer.*;
//import org.camunda.bpm.engine.impl.variable.serializer.jpa.EntityManagerSession;
//import org.camunda.bpm.engine.impl.variable.serializer.jpa.EntityManagerSessionFactory;
//import org.camunda.bpm.engine.impl.variable.serializer.jpa.JPAVariableSerializer;
//import org.camunda.bpm.engine.test.mock.MocksResolverFactory;
//import org.camunda.bpm.engine.variable.Variables;
//import org.camunda.bpm.engine.variable.type.ValueType;
//
//import javax.sql.DataSource;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.nio.charset.Charset;
//import java.sql.Connection;
//import java.sql.DatabaseMetaData;
//import java.sql.SQLException;
//import java.text.ParseException;
//import java.util.*;
//import java.util.concurrent.CopyOnWriteArraySet;
//
//public abstract class ProcessEngineConfigurationImpl extends ProcessEngineConfiguration
//{
//  protected static final ConfigurationLogger LOG = ConfigurationLogger.CONFIG_LOGGER;
//  public static final String DB_SCHEMA_UPDATE_CREATE = "create";
//  public static final String DB_SCHEMA_UPDATE_DROP_CREATE = "drop-create";
//  public static final int HISTORYLEVEL_NONE = HistoryLevel.HISTORY_LEVEL_NONE.getId();
//  public static final int HISTORYLEVEL_ACTIVITY = HistoryLevel.HISTORY_LEVEL_ACTIVITY.getId();
//  public static final int HISTORYLEVEL_AUDIT = HistoryLevel.HISTORY_LEVEL_AUDIT.getId();
//  public static final int HISTORYLEVEL_FULL = HistoryLevel.HISTORY_LEVEL_FULL.getId();
//  public static final String DEFAULT_WS_SYNC_FACTORY = "org.camunda.bpm.engine.impl.webservice.CxfWebServiceClientFactory";
//  public static final String DEFAULT_MYBATIS_MAPPING_FILE = "org/camunda/bpm/engine/impl/mapping/mappings.xml";
//  public static final int DEFAULT_FAILED_JOB_LISTENER_MAX_RETRIES = 3;
//  public static SqlSessionFactory cachedSqlSessionFactory;
//  protected RepositoryService repositoryService = new RepositoryServiceImpl();
//  protected RuntimeService runtimeService = new RuntimeServiceImpl();
//  protected HistoryService historyService = new HistoryServiceImpl();
//  protected IdentityService identityService = new IdentityServiceImpl();
//  protected TaskService taskService = new TaskServiceImpl();
//  protected FormService formService = new FormServiceImpl();
//  protected ManagementService managementService = new ManagementServiceImpl();
//  protected AuthorizationService authorizationService = new AuthorizationServiceImpl();
//  protected CaseService caseService = new CaseServiceImpl();
//  protected FilterService filterService = new FilterServiceImpl();
//  protected ExternalTaskService externalTaskService = new ExternalTaskServiceImpl();
//  protected DecisionService decisionService = new DecisionServiceImpl();
//  protected OptimizeService optimizeService = new OptimizeService();
//  protected List<CommandInterceptor> customPreCommandInterceptorsTxRequired;
//  protected List<CommandInterceptor> customPostCommandInterceptorsTxRequired;
//  protected List<CommandInterceptor> commandInterceptorsTxRequired;
//  protected CommandExecutor commandExecutorTxRequired;
//  protected List<CommandInterceptor> customPreCommandInterceptorsTxRequiresNew;
//  protected List<CommandInterceptor> customPostCommandInterceptorsTxRequiresNew;
//  protected List<CommandInterceptor> commandInterceptorsTxRequiresNew;
//  protected CommandExecutor commandExecutorTxRequiresNew;
//  protected CommandExecutor commandExecutorSchemaOperations;
//  protected List<SessionFactory> customSessionFactories;
//  protected DbSqlSessionFactory dbSqlSessionFactory;
//  protected Map<Class<?>, SessionFactory> sessionFactories;
//  protected List<Deployer> customPreDeployers;
//  protected List<Deployer> customPostDeployers;
//  protected List<Deployer> deployers;
//  protected DeploymentCache deploymentCache;
//  protected CacheFactory cacheFactory;
//  protected int cacheCapacity = 1000;
//  protected boolean enableFetchProcessDefinitionDescription = true;
//  protected List<JobHandler> customJobHandlers;
//  protected Map<String, JobHandler> jobHandlers;
//  protected JobExecutor jobExecutor;
//  protected PriorityProvider<JobDeclaration<?, ?>> jobPriorityProvider;
//  protected PriorityProvider<ExternalTaskActivityBehavior> externalTaskPriorityProvider;
//  protected SqlSessionFactory sqlSessionFactory;
//  protected TransactionFactory transactionFactory;
//  protected IdGenerator idGenerator;
//  protected DataSource idGeneratorDataSource;
//  protected String idGeneratorDataSourceJndiName;
//  protected Map<String, IncidentHandler> incidentHandlers;
//  protected List<IncidentHandler> customIncidentHandlers;
//  protected Map<String, BatchJobHandler<?>> batchHandlers;
//  protected List<BatchJobHandler<?>> customBatchJobHandlers;
//  protected int batchJobsPerSeed = 100;
//
//  protected int invocationsPerBatchJob = 1;
//
//  protected int batchPollTime = 30;
//
//  protected long batchJobPriority = DefaultJobPriorityProvider.DEFAULT_PRIORITY;
//  protected List<FormEngine> customFormEngines;
//  protected Map<String, FormEngine> formEngines;
//  protected List<AbstractFormFieldType> customFormTypes;
//  protected FormTypes formTypes;
//  protected FormValidators formValidators;
//  protected Map<String, Class<? extends FormFieldValidator>> customFormFieldValidators;
//  protected List<TypedValueSerializer> customPreVariableSerializers;
//  protected List<TypedValueSerializer> customPostVariableSerializers;
//  protected VariableSerializers variableSerializers;
//  protected VariableSerializerFactory fallbackSerializerFactory;
//  protected String defaultSerializationFormat = Variables.SerializationDataFormats.JAVA.getName();
//  protected boolean javaSerializationFormatEnabled = false;
//  protected String defaultCharsetName = null;
//  protected Charset defaultCharset = null;
//  protected ExpressionManager expressionManager;
//  protected ScriptingEngines scriptingEngines;
//  protected List<ResolverFactory> resolverFactories;
//  protected ScriptingEnvironment scriptingEnvironment;
//  protected List<ScriptEnvResolver> scriptEnvResolvers;
//  protected ScriptFactory scriptFactory;
//  protected boolean autoStoreScriptVariables = false;
//  protected boolean enableScriptCompilation = true;
//  protected boolean enableScriptEngineCaching = true;
//  protected boolean enableFetchScriptEngineFromProcessApplication = true;
//
//  protected boolean cmmnEnabled = true;
//  protected boolean dmnEnabled = true;
//
//  protected boolean enableGracefulDegradationOnContextSwitchFailure = true;
//  protected BusinessCalendarManager businessCalendarManager;
//  protected String wsSyncFactoryClassName = "org.camunda.bpm.engine.impl.webservice.CxfWebServiceClientFactory";
//  protected CommandContextFactory commandContextFactory;
//  protected TransactionContextFactory transactionContextFactory;
//  protected BpmnParseFactory bpmnParseFactory;
//  protected CmmnTransformFactory cmmnTransformFactory;
//  protected DefaultCmmnElementHandlerRegistry cmmnElementHandlerRegistry;
//  protected DefaultDmnEngineConfiguration dmnEngineConfiguration;
//  protected DmnEngine dmnEngine;
//  protected HistoryLevel historyLevel;
//  protected List<HistoryLevel> historyLevels;
//  protected List<HistoryLevel> customHistoryLevels;
//  protected List<BpmnParseListener> preParseListeners;
//  protected List<BpmnParseListener> postParseListeners;
//  protected List<CmmnTransformListener> customPreCmmnTransformListeners;
//  protected List<CmmnTransformListener> customPostCmmnTransformListeners;
//  protected Map<Object, Object> beans;
//  protected boolean isDbIdentityUsed = true;
//  protected boolean isDbHistoryUsed = true;
//  protected DelegateInterceptor delegateInterceptor;
//  protected CommandInterceptor actualCommandExecutor;
//  protected RejectedJobsHandler customRejectedJobsHandler;
//  protected Map<String, EventHandler> eventHandlers;
//  protected List<EventHandler> customEventHandlers;
//  protected FailedJobCommandFactory failedJobCommandFactory;
//  protected String databaseTablePrefix = "";
//
//  protected String databaseSchema = null;
//
//  protected boolean isCreateDiagramOnDeploy = false;
//  protected ProcessApplicationManager processApplicationManager;
//  protected CorrelationHandler correlationHandler;
//  protected ConditionHandler conditionHandler;
//  protected SessionFactory identityProviderSessionFactory;
//  protected PasswordEncryptor passwordEncryptor;
//  protected List<PasswordEncryptor> customPasswordChecker;
//  protected PasswordManager passwordManager;
//  protected SaltGenerator saltGenerator;
//  protected Set<String> registeredDeployments;
//  protected ResourceAuthorizationProvider resourceAuthorizationProvider;
//  protected List<ProcessEnginePlugin> processEnginePlugins = new ArrayList();
//  protected HistoryEventProducer historyEventProducer;
//  protected CmmnHistoryEventProducer cmmnHistoryEventProducer;
//  protected DmnHistoryEventProducer dmnHistoryEventProducer;
//  protected HistoryEventHandler historyEventHandler;
//  protected boolean isExecutionTreePrefetchEnabled = true;
//
//  protected boolean isDeploymentLockUsed = true;
//
//  protected boolean isDeploymentSynchronized = true;
//
//  protected boolean isDbEntityCacheReuseEnabled = false;
//
//  protected boolean isInvokeCustomVariableListeners = true;
//  protected ProcessEngineImpl processEngine;
//  protected ArtifactFactory artifactFactory;
//  protected DbEntityCacheKeyMapping dbEntityCacheKeyMapping = DbEntityCacheKeyMapping.defaultEntityCacheKeyMapping();
//  protected MetricsRegistry metricsRegistry;
//  protected DbMetricsReporter dbMetricsReporter;
//  protected boolean isMetricsEnabled = true;
//  protected boolean isDbMetricsReporterActivate = true;
//  protected MetricsReporterIdProvider metricsReporterIdProvider;
//  protected boolean enableExpressionsInAdhocQueries = false;
//  protected boolean enableExpressionsInStoredQueries = true;
//
//  protected boolean enableXxeProcessing = false;
//
//  protected boolean restrictUserOperationLogToAuthenticatedUsers = true;
//
//  protected boolean disableStrictCallActivityValidation = false;
//
//  protected boolean isBpmnStacktraceVerbose = false;
//
//  protected boolean forceCloseMybatisConnectionPool = true;
//
//  protected TenantIdProvider tenantIdProvider = null;
//
//  protected List<CommandChecker> commandCheckers = null;
//  protected List<String> adminGroups;
//  protected List<String> adminUsers;
//  protected MigrationActivityMatcher migrationActivityMatcher;
//  protected List<MigrationActivityValidator> customPreMigrationActivityValidators;
//  protected List<MigrationActivityValidator> customPostMigrationActivityValidators;
//  protected MigrationInstructionGenerator migrationInstructionGenerator;
//  protected List<MigrationInstructionValidator> customPreMigrationInstructionValidators;
//  protected List<MigrationInstructionValidator> customPostMigrationInstructionValidators;
//  protected List<MigrationInstructionValidator> migrationInstructionValidators;
//  protected List<MigratingActivityInstanceValidator> customPreMigratingActivityInstanceValidators;
//  protected List<MigratingActivityInstanceValidator> customPostMigratingActivityInstanceValidators;
//  protected List<MigratingActivityInstanceValidator> migratingActivityInstanceValidators;
//  protected List<MigratingTransitionInstanceValidator> migratingTransitionInstanceValidators;
//  protected List<MigratingCompensationInstanceValidator> migratingCompensationInstanceValidators;
//  protected Permission defaultUserPermissionForTask;
//  protected boolean isUseSharedSqlSessionFactory = false;
//  protected String historyCleanupBatchWindowStartTime;
//  protected String historyCleanupBatchWindowEndTime = "00:00";
//  protected Date historyCleanupBatchWindowStartTimeAsDate;
//  protected Date historyCleanupBatchWindowEndTimeAsDate;
//  protected Map<Integer, BatchWindowConfiguration> historyCleanupBatchWindows = new HashMap();
//  protected String mondayHistoryCleanupBatchWindowStartTime;
//  protected String mondayHistoryCleanupBatchWindowEndTime;
//  protected String tuesdayHistoryCleanupBatchWindowStartTime;
//  protected String tuesdayHistoryCleanupBatchWindowEndTime;
//  protected String wednesdayHistoryCleanupBatchWindowStartTime;
//  protected String wednesdayHistoryCleanupBatchWindowEndTime;
//  protected String thursdayHistoryCleanupBatchWindowStartTime;
//  protected String thursdayHistoryCleanupBatchWindowEndTime;
//  protected String fridayHistoryCleanupBatchWindowStartTime;
//  protected String fridayHistoryCleanupBatchWindowEndTime;
//  protected String saturdayHistoryCleanupBatchWindowStartTime;
//  protected String saturdayHistoryCleanupBatchWindowEndTime;
//  protected String sundayHistoryCleanupBatchWindowStartTime;
//  protected String sundayHistoryCleanupBatchWindowEndTime;
//  protected int historyCleanupDegreeOfParallelism = 1;
//  protected String batchOperationHistoryTimeToLive;
//  protected Map<String, String> batchOperationsForHistoryCleanup;
//  protected Map<String, Integer> parsedBatchOperationsForHistoryCleanup;
//  protected BatchWindowManager batchWindowManager = new DefaultBatchWindowManager();
//  protected HistoryRemovalTimeProvider historyRemovalTimeProvider;
//  protected String historyRemovalTimeStrategy;
//  protected String historyCleanupStrategy;
//  private int historyCleanupBatchSize = 500;
//
//  private int historyCleanupBatchThreshold = 10;
//
//  private boolean historyCleanupMetricsEnabled = true;
//
//  private int failedJobListenerMaxRetries = 3;
//  protected String failedJobRetryTimeCycle;
//  protected int loginMaxAttempts = 10;
//  protected int loginDelayFactor = 2;
//  protected int loginDelayMaxTime = 60;
//  protected int loginDelayBase = 3;
//
//  protected static Properties databaseTypeMappings = getDefaultDatabaseTypeMappings();
//  protected static final String MY_SQL_PRODUCT_NAME = "MySQL";
//  protected static final String MARIA_DB_PRODUCT_NAME = "MariaDB";
//
//  public ProcessEngine buildProcessEngine()
//  {
//    init();
//    this.processEngine = new ProcessEngineImpl(this);
//    invokePostProcessEngineBuild(this.processEngine);
//    return this.processEngine;
//  }
//
//  protected void init()
//  {
//    invokePreInit();
//    initDefaultCharset();
//    initHistoryLevel();
//    initHistoryEventProducer();
//    initCmmnHistoryEventProducer();
//    initDmnHistoryEventProducer();
//    initHistoryEventHandler();
//    initExpressionManager();
//    initBeans();
//    initArtifactFactory();
//    initFormEngines();
//    initFormTypes();
//    initFormFieldValidators();
//    initScripting();
//    initDmnEngine();
//    initBusinessCalendarManager();
//    initCommandContextFactory();
//    initTransactionContextFactory();
//    initCommandExecutors();
//    initServices();
//    initIdGenerator();
//    initFailedJobCommandFactory();
//    initDeployers();
//    initJobProvider();
//    initExternalTaskPriorityProvider();
//    initBatchHandlers();
//    initJobExecutor();
//    initDataSource();
//    initTransactionFactory();
//    initSqlSessionFactory();
//    initIdentityProviderSessionFactory();
//    initSessionFactories();
//    initValueTypeResolver();
//    initSerialization();
//    initJpa();
//    initDelegateInterceptor();
//    initEventHandlers();
//    initProcessApplicationManager();
//    initCorrelationHandler();
//    initConditionHandler();
//    initIncidentHandlers();
//    initPasswordDigest();
//    initDeploymentRegistration();
//    initResourceAuthorizationProvider();
//    initMetrics();
//    initMigration();
//    initCommandCheckers();
//    initDefaultUserPermissionForTask();
//    initHistoryRemovalTime();
//    initHistoryCleanup();
//    initAdminUser();
//    initAdminGroups();
//    invokePostInit();
//  }
//
//  public void initHistoryRemovalTime() {
//    initHistoryRemovalTimeProvider();
//    initHistoryRemovalTimeStrategy();
//  }
//
//  public void initHistoryRemovalTimeStrategy() {
//    if (this.historyRemovalTimeStrategy == null) {
//      this.historyRemovalTimeStrategy = "end";
//    }
//
//    if ((!"start".equals(this.historyRemovalTimeStrategy)) &&
//            (!"end"
//                    .equals(this.historyRemovalTimeStrategy)) &&
//            (!"none"
//                    .equals(this.historyRemovalTimeStrategy)))
//    {
//      throw LOG.invalidPropertyValue("historyRemovalTimeStrategy", String.valueOf(this.historyRemovalTimeStrategy),
//              String.format("history removal time strategy must be set to '%s', '%s' or '%s'", new Object[] { "start", "end", "none" }));
//    }
//  }
//
//  public void initHistoryRemovalTimeProvider()
//  {
//    if (this.historyRemovalTimeProvider == null)
//      this.historyRemovalTimeProvider = new DefaultHistoryRemovalTimeProvider();
//  }
//
//  public void initHistoryCleanup()
//  {
//    initHistoryCleanupStrategy();
//
//    if ((this.historyCleanupDegreeOfParallelism < 1) || (this.historyCleanupDegreeOfParallelism > 8)) {
//      throw LOG.invalidPropertyValue("historyCleanupDegreeOfParallelism", String.valueOf(this.historyCleanupDegreeOfParallelism),
//              String.format("value for number of threads for history cleanup should be between 1 and %s", new Object[] {
//                      Integer.valueOf(8) }));
//    }
//
//    if (this.historyCleanupBatchWindowStartTime != null) {
//      initHistoryCleanupBatchWindowStartTime();
//    }
//
//    if (this.historyCleanupBatchWindowEndTime != null) {
//      initHistoryCleanupBatchWindowEndTime();
//    }
//
//    initHistoryCleanupBatchWindowsMap();
//
//    if ((this.historyCleanupBatchSize > 500) || (this.historyCleanupBatchSize <= 0)) {
//      throw LOG.invalidPropertyValue("historyCleanupBatchSize", String.valueOf(this.historyCleanupBatchSize),
//              String.format("value for batch size should be between 1 and %s", new Object[] {
//                      Integer.valueOf(500) }));
//    }
//
//    if (this.historyCleanupBatchThreshold < 0) {
//      throw LOG.invalidPropertyValue("historyCleanupBatchThreshold", String.valueOf(this.historyCleanupBatchThreshold), "History cleanup batch threshold cannot be negative.");
//    }
//
//    initBatchOperationsHistoryTimeToLive();
//  }
//
//  protected void initHistoryCleanupStrategy() {
//    if (this.historyCleanupStrategy == null) {
//      this.historyCleanupStrategy = "removalTimeBased";
//    }
//
//    if ((!"removalTimeBased".equals(this.historyCleanupStrategy)) &&
//            (!"endTimeBased"
//                    .equals(this.historyCleanupStrategy)))
//    {
//      throw LOG.invalidPropertyValue("historyCleanupStrategy", String.valueOf(this.historyCleanupStrategy),
//              String.format("history cleanup strategy must be either set to '%s' or '%s'", new Object[] { "removalTimeBased", "endTimeBased" }));
//    }
//
//    if (("removalTimeBased".equals(this.historyCleanupStrategy)) &&
//            ("none"
//                    .equals(this.historyRemovalTimeStrategy)))
//    {
//      throw LOG.invalidPropertyValue("historyRemovalTimeStrategy", String.valueOf(this.historyRemovalTimeStrategy),
//              String.format("history removal time strategy cannot be set to '%s' in conjunction with '%s' history cleanup strategy", new Object[] { "none", "removalTimeBased" }));
//    }
//  }
//
//  private void initHistoryCleanupBatchWindowsMap()
//  {
//    if ((this.mondayHistoryCleanupBatchWindowStartTime != null) || (this.mondayHistoryCleanupBatchWindowEndTime != null)) {
//      this.historyCleanupBatchWindows.put(Integer.valueOf(2), new BatchWindowConfiguration(this.mondayHistoryCleanupBatchWindowStartTime, this.mondayHistoryCleanupBatchWindowEndTime));
//    }
//
//    if ((this.tuesdayHistoryCleanupBatchWindowStartTime != null) || (this.tuesdayHistoryCleanupBatchWindowEndTime != null)) {
//      this.historyCleanupBatchWindows.put(Integer.valueOf(3), new BatchWindowConfiguration(this.tuesdayHistoryCleanupBatchWindowStartTime, this.tuesdayHistoryCleanupBatchWindowEndTime));
//    }
//
//    if ((this.wednesdayHistoryCleanupBatchWindowStartTime != null) || (this.wednesdayHistoryCleanupBatchWindowEndTime != null)) {
//      this.historyCleanupBatchWindows.put(Integer.valueOf(4), new BatchWindowConfiguration(this.wednesdayHistoryCleanupBatchWindowStartTime, this.wednesdayHistoryCleanupBatchWindowEndTime));
//    }
//
//    if ((this.thursdayHistoryCleanupBatchWindowStartTime != null) || (this.thursdayHistoryCleanupBatchWindowEndTime != null)) {
//      this.historyCleanupBatchWindows.put(Integer.valueOf(5), new BatchWindowConfiguration(this.thursdayHistoryCleanupBatchWindowStartTime, this.thursdayHistoryCleanupBatchWindowEndTime));
//    }
//
//    if ((this.fridayHistoryCleanupBatchWindowStartTime != null) || (this.fridayHistoryCleanupBatchWindowEndTime != null)) {
//      this.historyCleanupBatchWindows.put(Integer.valueOf(6), new BatchWindowConfiguration(this.fridayHistoryCleanupBatchWindowStartTime, this.fridayHistoryCleanupBatchWindowEndTime));
//    }
//
//    if ((this.saturdayHistoryCleanupBatchWindowStartTime != null) || (this.saturdayHistoryCleanupBatchWindowEndTime != null)) {
//      this.historyCleanupBatchWindows.put(Integer.valueOf(7), new BatchWindowConfiguration(this.saturdayHistoryCleanupBatchWindowStartTime, this.saturdayHistoryCleanupBatchWindowEndTime));
//    }
//
//    if ((this.sundayHistoryCleanupBatchWindowStartTime != null) || (this.sundayHistoryCleanupBatchWindowEndTime != null))
//      this.historyCleanupBatchWindows.put(Integer.valueOf(1), new BatchWindowConfiguration(this.sundayHistoryCleanupBatchWindowStartTime, this.sundayHistoryCleanupBatchWindowEndTime));
//  }
//
//  protected void initBatchOperationsHistoryTimeToLive()
//  {
//    try {
//      ParseUtil.parseHistoryTimeToLive(this.batchOperationHistoryTimeToLive);
//    } catch (Exception e) {
//      throw LOG.invalidPropertyValue("batchOperationHistoryTimeToLive", this.batchOperationHistoryTimeToLive, e);
//    }
//
//    if (this.batchOperationsForHistoryCleanup == null)
//      this.batchOperationsForHistoryCleanup = new HashMap();
//    else {
//      for (String batchOperation : this.batchOperationsForHistoryCleanup.keySet()) {
//        String timeToLive = (String)this.batchOperationsForHistoryCleanup.get(batchOperation);
//        if (!this.batchHandlers.keySet().contains(batchOperation)) {
//          LOG.invalidBatchOperation(batchOperation, timeToLive);
//        }
//        try
//        {
//          ParseUtil.parseHistoryTimeToLive(timeToLive);
//        } catch (Exception e) {
//          throw LOG.invalidPropertyValue("history time to live for " + batchOperation + " batch operations", timeToLive, e);
//        }
//      }
//    }
//
//    if ((this.batchHandlers != null) && (this.batchOperationHistoryTimeToLive != null))
//    {
//      for (String batchOperation : this.batchHandlers.keySet()) {
//        if (!this.batchOperationsForHistoryCleanup.containsKey(batchOperation)) {
//          this.batchOperationsForHistoryCleanup.put(batchOperation, this.batchOperationHistoryTimeToLive);
//        }
//      }
//    }
//
//    this.parsedBatchOperationsForHistoryCleanup = new HashMap();
//    if (this.batchOperationsForHistoryCleanup != null)
//      for (String operation : this.batchOperationsForHistoryCleanup.keySet()) {
//        Integer historyTimeToLive = ParseUtil.parseHistoryTimeToLive((String)this.batchOperationsForHistoryCleanup.get(operation));
//        this.parsedBatchOperationsForHistoryCleanup.put(operation, historyTimeToLive);
//      }
//  }
//
//  private void initHistoryCleanupBatchWindowEndTime()
//  {
//    try {
//      this.historyCleanupBatchWindowEndTimeAsDate = HistoryCleanupHelper.parseTimeConfiguration(this.historyCleanupBatchWindowEndTime);
//    } catch (ParseException e) {
//      throw LOG.invalidPropertyValue("historyCleanupBatchWindowEndTime", this.historyCleanupBatchWindowEndTime);
//    }
//  }
//
//  private void initHistoryCleanupBatchWindowStartTime() {
//    try {
//      this.historyCleanupBatchWindowStartTimeAsDate = HistoryCleanupHelper.parseTimeConfiguration(this.historyCleanupBatchWindowStartTime);
//    } catch (ParseException e) {
//      throw LOG.invalidPropertyValue("historyCleanupBatchWindowStartTime", this.historyCleanupBatchWindowStartTime);
//    }
//  }
//
//  protected void invokePreInit() {
//    for (ProcessEnginePlugin plugin : this.processEnginePlugins) {
//      LOG.pluginActivated(plugin.toString(), getProcessEngineName());
//      plugin.preInit(this);
//    }
//  }
//
//  protected void invokePostInit() {
//    for (ProcessEnginePlugin plugin : this.processEnginePlugins)
//      plugin.postInit(this);
//  }
//
//  protected void invokePostProcessEngineBuild(ProcessEngine engine)
//  {
//    for (ProcessEnginePlugin plugin : this.processEnginePlugins)
//      plugin.postProcessEngineBuild(engine);
//  }
//
//  protected void initFailedJobCommandFactory()
//  {
//    if (this.failedJobCommandFactory == null) {
//      this.failedJobCommandFactory = new DefaultFailedJobCommandFactory();
//    }
//    if (this.postParseListeners == null) {
//      this.postParseListeners = new ArrayList();
//    }
//    this.postParseListeners.add(new DefaultFailedJobParseListener());
//  }
//
//  protected void initIncidentHandlers()
//  {
//    DefaultIncidentHandler failedJobIncidentHandler;
//    if (this.incidentHandlers == null) {
//      this.incidentHandlers = new HashMap();
//
//      failedJobIncidentHandler = new DefaultIncidentHandler("failedJob");
//      this.incidentHandlers.put(failedJobIncidentHandler.getIncidentHandlerType(), failedJobIncidentHandler);
//
//      DefaultIncidentHandler failedExternalTaskIncidentHandler = new DefaultIncidentHandler("failedExternalTask");
//      this.incidentHandlers.put(failedExternalTaskIncidentHandler.getIncidentHandlerType(), failedExternalTaskIncidentHandler);
//    }
//    if (this.customIncidentHandlers != null)
//      for (IncidentHandler incidentHandler : this.customIncidentHandlers)
//        this.incidentHandlers.put(incidentHandler.getIncidentHandlerType(), incidentHandler);
//  }
//
//  protected void initBatchHandlers()
//  {
//    MigrationBatchJobHandler migrationHandler;
//    if (this.batchHandlers == null) {
//      this.batchHandlers = new HashMap();
//
//      migrationHandler = new MigrationBatchJobHandler();
//      this.batchHandlers.put(migrationHandler.getType(), migrationHandler);
//
//      ModificationBatchJobHandler modificationHandler = new ModificationBatchJobHandler();
//      this.batchHandlers.put(modificationHandler.getType(), modificationHandler);
//
//      DeleteProcessInstancesJobHandler deleteProcessJobHandler = new DeleteProcessInstancesJobHandler();
//      this.batchHandlers.put(deleteProcessJobHandler.getType(), deleteProcessJobHandler);
//
//      DeleteHistoricProcessInstancesJobHandler deleteHistoricProcessInstancesJobHandler = new DeleteHistoricProcessInstancesJobHandler();
//      this.batchHandlers.put(deleteHistoricProcessInstancesJobHandler.getType(), deleteHistoricProcessInstancesJobHandler);
//
//      SetJobRetriesJobHandler setJobRetriesJobHandler = new SetJobRetriesJobHandler();
//      this.batchHandlers.put(setJobRetriesJobHandler.getType(), setJobRetriesJobHandler);
//
//      SetExternalTaskRetriesJobHandler setExternalTaskRetriesJobHandler = new SetExternalTaskRetriesJobHandler();
//      this.batchHandlers.put(setExternalTaskRetriesJobHandler.getType(), setExternalTaskRetriesJobHandler);
//
//      RestartProcessInstancesJobHandler restartProcessInstancesJobHandler = new RestartProcessInstancesJobHandler();
//      this.batchHandlers.put(restartProcessInstancesJobHandler.getType(), restartProcessInstancesJobHandler);
//
//      UpdateProcessInstancesSuspendStateJobHandler suspendProcessInstancesJobHandler = new UpdateProcessInstancesSuspendStateJobHandler();
//      this.batchHandlers.put(suspendProcessInstancesJobHandler.getType(), suspendProcessInstancesJobHandler);
//
//      DeleteHistoricDecisionInstancesJobHandler deleteHistoricDecisionInstancesJobHandler = new DeleteHistoricDecisionInstancesJobHandler();
//      this.batchHandlers.put(deleteHistoricDecisionInstancesJobHandler.getType(), deleteHistoricDecisionInstancesJobHandler);
//    }
//
//    if (this.customBatchJobHandlers != null)
//      for (BatchJobHandler customBatchJobHandler : this.customBatchJobHandlers)
//        this.batchHandlers.put(customBatchJobHandler.getType(), customBatchJobHandler);
//  }
//
//  protected abstract Collection<? extends CommandInterceptor> getDefaultCommandInterceptorsTxRequired();
//
//  protected abstract Collection<? extends CommandInterceptor> getDefaultCommandInterceptorsTxRequiresNew();
//
//  protected void initCommandExecutors()
//  {
//    initActualCommandExecutor();
//    initCommandInterceptorsTxRequired();
//    initCommandExecutorTxRequired();
//    initCommandInterceptorsTxRequiresNew();
//    initCommandExecutorTxRequiresNew();
//    initCommandExecutorDbSchemaOperations();
//  }
//
//  protected void initActualCommandExecutor() {
//    this.actualCommandExecutor = new CommandExecutorImpl();
//  }
//
//  protected void initCommandInterceptorsTxRequired() {
//    if (this.commandInterceptorsTxRequired == null) {
//      if (this.customPreCommandInterceptorsTxRequired != null)
//        this.commandInterceptorsTxRequired = new ArrayList(this.customPreCommandInterceptorsTxRequired);
//      else {
//        this.commandInterceptorsTxRequired = new ArrayList();
//      }
//      this.commandInterceptorsTxRequired.addAll(getDefaultCommandInterceptorsTxRequired());
//      if (this.customPostCommandInterceptorsTxRequired != null) {
//        this.commandInterceptorsTxRequired.addAll(this.customPostCommandInterceptorsTxRequired);
//      }
//      this.commandInterceptorsTxRequired.add(this.actualCommandExecutor);
//    }
//  }
//
//  protected void initCommandInterceptorsTxRequiresNew() {
//    if (this.commandInterceptorsTxRequiresNew == null) {
//      if (this.customPreCommandInterceptorsTxRequiresNew != null)
//        this.commandInterceptorsTxRequiresNew = new ArrayList(this.customPreCommandInterceptorsTxRequiresNew);
//      else {
//        this.commandInterceptorsTxRequiresNew = new ArrayList();
//      }
//      this.commandInterceptorsTxRequiresNew.addAll(getDefaultCommandInterceptorsTxRequiresNew());
//      if (this.customPostCommandInterceptorsTxRequiresNew != null) {
//        this.commandInterceptorsTxRequiresNew.addAll(this.customPostCommandInterceptorsTxRequiresNew);
//      }
//      this.commandInterceptorsTxRequiresNew.add(this.actualCommandExecutor);
//    }
//  }
//
//  protected void initCommandExecutorTxRequired() {
//    if (this.commandExecutorTxRequired == null)
//      this.commandExecutorTxRequired = initInterceptorChain(this.commandInterceptorsTxRequired);
//  }
//
//  protected void initCommandExecutorTxRequiresNew()
//  {
//    if (this.commandExecutorTxRequiresNew == null)
//      this.commandExecutorTxRequiresNew = initInterceptorChain(this.commandInterceptorsTxRequiresNew);
//  }
//
//  protected void initCommandExecutorDbSchemaOperations()
//  {
//    if (this.commandExecutorSchemaOperations == null)
//    {
//      this.commandExecutorSchemaOperations = this.commandExecutorTxRequired;
//    }
//  }
//
//  protected CommandInterceptor initInterceptorChain(List<CommandInterceptor> chain) {
//    if ((chain == null) || (chain.isEmpty())) {
//      throw new ProcessEngineException("invalid command interceptor chain configuration: " + chain);
//    }
//    for (int i = 0; i < chain.size() - 1; i++) {
//      ((CommandInterceptor)chain.get(i)).setNext((CommandExecutor)chain.get(i + 1));
//    }
//    return (CommandInterceptor)chain.get(0);
//  }
//
//  protected void initServices()
//  {
//    initService(this.repositoryService);
//    initService(this.runtimeService);
//    initService(this.historyService);
//    initService(this.identityService);
//    initService(this.taskService);
//    initService(this.formService);
//    initService(this.managementService);
//    initService(this.authorizationService);
//    initService(this.caseService);
//    initService(this.filterService);
//    initService(this.externalTaskService);
//    initService(this.decisionService);
//    initService(this.optimizeService);
//  }
//
//  protected void initService(Object service) {
//    if ((service instanceof ServiceImpl)) {
//      ((ServiceImpl)service).setCommandExecutor(this.commandExecutorTxRequired);
//    }
//    if ((service instanceof RepositoryServiceImpl))
//      ((RepositoryServiceImpl)service).setDeploymentCharset(getDefaultCharset());
//  }
//
//  protected void initDataSource()
//  {
//    System.out.println("--------------------------oneday--------------");
//    //if (this.dataSource == null) {
//      /*if (this.dataSourceJndiName != null) {
//        try {
//          this.dataSource = ((DataSource)new InitialContext().lookup(this.dataSourceJndiName));
//        } catch (Exception e) {
//          throw new ProcessEngineException("couldn't lookup datasource from " + this.dataSourceJndiName + ": " + e.getMessage(), e);
//        }
//      }*/
//    //if (this.jdbcUrl != null) {
//    if (1 == 1) {
//      if ((this.jdbcDriver == null) || (this.jdbcUrl == null) || (this.jdbcUsername == null)) {
//        throw new ProcessEngineException("DataSource or JDBC properties have to be specified in a process engine configuration");
//      }
//
//       /* PooledDataSource pooledDataSource = new PooledDataSource(
//          ReflectUtil.getClassLoader(), this.jdbcDriver, this.jdbcUrl, this.jdbcUsername, this.jdbcPassword);*/
//
//      PooledDataSource pooledDataSource = new PooledDataSource(
//              ReflectUtil.getClassLoader(),
//              this.jdbcDriver,
//              "jdbc:mysql://192.168.25.129:3306/medicine-wms-lzh",
//              "root",
//              "666");
//
//      if (this.jdbcMaxActiveConnections > 0) {
//        pooledDataSource.setPoolMaximumActiveConnections(this.jdbcMaxActiveConnections);
//      }
//      if (this.jdbcMaxIdleConnections > 0) {
//        pooledDataSource.setPoolMaximumIdleConnections(this.jdbcMaxIdleConnections);
//      }
//      if (this.jdbcMaxCheckoutTime > 0) {
//        pooledDataSource.setPoolMaximumCheckoutTime(this.jdbcMaxCheckoutTime);
//      }
//      if (this.jdbcMaxWaitTime > 0) {
//        pooledDataSource.setPoolTimeToWait(this.jdbcMaxWaitTime);
//      }
//      if (this.jdbcPingEnabled == true) {
//        pooledDataSource.setPoolPingEnabled(true);
//        if (this.jdbcPingQuery != null) {
//          pooledDataSource.setPoolPingQuery(this.jdbcPingQuery);
//        }
//        pooledDataSource.setPoolPingConnectionsNotUsedFor(this.jdbcPingConnectionNotUsedFor);
//      }
//      this.dataSource = pooledDataSource;
//    }
//
//    if ((this.dataSource instanceof PooledDataSource))
//    {
//      ((PooledDataSource)this.dataSource).forceCloseAll();
//    }
//    //}
//
//    if (this.databaseType == null)
//      initDatabaseType();
//  }
//
//  protected static Properties getDefaultDatabaseTypeMappings()
//  {
//    Properties databaseTypeMappings = new Properties();
//    databaseTypeMappings.setProperty("H2", "h2");
//    databaseTypeMappings.setProperty("MySQL", "mysql");
//    databaseTypeMappings.setProperty("MariaDB", "mariadb");
//    databaseTypeMappings.setProperty("Oracle", "oracle");
//    databaseTypeMappings.setProperty("PostgreSQL", "postgres");
//    databaseTypeMappings.setProperty("Microsoft SQL Server", "mssql");
//    databaseTypeMappings.setProperty("DB2", "db2");
//    databaseTypeMappings.setProperty("DB2", "db2");
//    databaseTypeMappings.setProperty("DB2/NT", "db2");
//    databaseTypeMappings.setProperty("DB2/NT64", "db2");
//    databaseTypeMappings.setProperty("DB2 UDP", "db2");
//    databaseTypeMappings.setProperty("DB2/LINUX", "db2");
//    databaseTypeMappings.setProperty("DB2/LINUX390", "db2");
//    databaseTypeMappings.setProperty("DB2/LINUXX8664", "db2");
//    databaseTypeMappings.setProperty("DB2/LINUXZ64", "db2");
//    databaseTypeMappings.setProperty("DB2/400 SQL", "db2");
//    databaseTypeMappings.setProperty("DB2/6000", "db2");
//    databaseTypeMappings.setProperty("DB2 UDB iSeries", "db2");
//    databaseTypeMappings.setProperty("DB2/AIX64", "db2");
//    databaseTypeMappings.setProperty("DB2/HPUX", "db2");
//    databaseTypeMappings.setProperty("DB2/HP64", "db2");
//    databaseTypeMappings.setProperty("DB2/SUN", "db2");
//    databaseTypeMappings.setProperty("DB2/SUN64", "db2");
//    databaseTypeMappings.setProperty("DB2/PTX", "db2");
//    databaseTypeMappings.setProperty("DB2/2", "db2");
//    return databaseTypeMappings;
//  }
//
//  public void initDatabaseType() {
//    Connection connection = null;
//    try {
//      connection = this.dataSource.getConnection();
//      DatabaseMetaData databaseMetaData = connection.getMetaData();
//      String databaseProductName = databaseMetaData.getDatabaseProductName();
//      if ("MySQL".equals(databaseProductName)) {
//        databaseProductName = checkForMariaDb(databaseMetaData, databaseProductName);
//      }
//      LOG.debugDatabaseproductName(databaseProductName);
//      this.databaseType = databaseTypeMappings.getProperty(databaseProductName);
//      EnsureUtil.ensureNotNull("couldn't deduct database type from database product name '" + databaseProductName + "'", "databaseType", this.databaseType);
//      LOG.debugDatabaseType(this.databaseType);
//    }
//    catch (SQLException e) {
//      e.printStackTrace();
//    } finally {
//      try {
//        if (connection != null)
//          connection.close();
//      }
//      catch (SQLException e) {
//        e.printStackTrace();
//      }
//    }
//  }
//
//  protected String checkForMariaDb(DatabaseMetaData databaseMetaData, String databaseName)
//  {
//    try
//    {
//      String databaseProductVersion = databaseMetaData.getDatabaseProductVersion();
//      if ((databaseProductVersion != null) && (databaseProductVersion.toLowerCase().contains("mariadb")))
//        return "MariaDB";
//    }
//    catch (SQLException localSQLException)
//    {
//    }
//    try {
//      String driverName = databaseMetaData.getDriverName();
//      if ((driverName != null) && (driverName.toLowerCase().contains("mariadb")))
//        return "MariaDB";
//    }
//    catch (SQLException localSQLException1)
//    {
//    }
//    String metaDataClassName = databaseMetaData.getClass().getName();
//    if ((metaDataClassName != null) && (metaDataClassName.toLowerCase().contains("mariadb"))) {
//      return "MariaDB";
//    }
//
//    return databaseName;
//  }
//
//  protected void initTransactionFactory()
//  {
//    if (this.transactionFactory == null)
//      if (this.transactionsExternallyManaged)
//        this.transactionFactory = new ManagedTransactionFactory();
//      else
//        this.transactionFactory = new JdbcTransactionFactory();
//  }
//
//  protected void initSqlSessionFactory()
//  {
//    synchronized (ProcessEngineConfigurationImpl.class)
//    {
//      if (this.isUseSharedSqlSessionFactory) {
//        this.sqlSessionFactory = cachedSqlSessionFactory;
//      }
//
//      if (this.sqlSessionFactory == null) {
//        InputStream inputStream = null;
//        try {
//          inputStream = getMyBatisXmlConfigurationSteam();
//
//          Environment environment = new Environment("default", this.transactionFactory, this.dataSource);
//          Reader reader = new InputStreamReader(inputStream);
//
//          Properties properties = new Properties();
//
//          if (this.isUseSharedSqlSessionFactory)
//            properties.put("prefix", "${@org.camunda.bpm.engine.impl.context.Context@getProcessEngineConfiguration().databaseTablePrefix}");
//          else {
//            properties.put("prefix", this.databaseTablePrefix);
//          }
//
//          initSqlSessionFactoryProperties(properties, this.databaseTablePrefix, this.databaseType);
//
//          XMLConfigBuilder parser = new XMLConfigBuilder(reader, "", properties);
//          Configuration configuration = parser.getConfiguration();
//          configuration.setEnvironment(environment);
//          configuration = parser.parse();
//
//          configuration.setDefaultStatementTimeout(this.jdbcStatementTimeout);
//
//          if (isJdbcBatchProcessing()) {
//            configuration.setDefaultExecutorType(ExecutorType.BATCH);
//          }
//
//          this.sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
//
//          if (this.isUseSharedSqlSessionFactory) {
//            cachedSqlSessionFactory = this.sqlSessionFactory;
//          }
//        }
//        catch (Exception e)
//        {
//          throw new ProcessEngineException("Error while building ibatis SqlSessionFactory: " + e.getMessage(), e);
//        } finally {
//          IoUtil.closeSilently(inputStream);
//        }
//      }
//    }
//  }
//
//  public static void initSqlSessionFactoryProperties(Properties properties, String databaseTablePrefix, String databaseType)
//  {
//    if (databaseType != null) {
//      properties.put("limitBefore", DbSqlSessionFactory.databaseSpecificLimitBeforeStatements.get(databaseType));
//      properties.put("limitAfter", DbSqlSessionFactory.databaseSpecificLimitAfterStatements.get(databaseType));
//      properties.put("limitBeforeWithoutOffset", DbSqlSessionFactory.databaseSpecificLimitBeforeWithoutOffsetStatements.get(databaseType));
//      properties.put("limitAfterWithoutOffset", DbSqlSessionFactory.databaseSpecificLimitAfterWithoutOffsetStatements.get(databaseType));
//
//      properties.put("optimizeLimitBeforeWithoutOffset", DbSqlSessionFactory.optimizeDatabaseSpecificLimitBeforeWithoutOffsetStatements.get(databaseType));
//      properties.put("optimizeLimitAfterWithoutOffset", DbSqlSessionFactory.optimizeDatabaseSpecificLimitAfterWithoutOffsetStatements.get(databaseType));
//      properties.put("innerLimitAfter", DbSqlSessionFactory.databaseSpecificInnerLimitAfterStatements.get(databaseType));
//      properties.put("limitBetween", DbSqlSessionFactory.databaseSpecificLimitBetweenStatements.get(databaseType));
//      properties.put("limitBetweenFilter", DbSqlSessionFactory.databaseSpecificLimitBetweenFilterStatements.get(databaseType));
//      properties.put("orderBy", DbSqlSessionFactory.databaseSpecificOrderByStatements.get(databaseType));
//      properties.put("limitBeforeNativeQuery", DbSqlSessionFactory.databaseSpecificLimitBeforeNativeQueryStatements.get(databaseType));
//      properties.put("distinct", DbSqlSessionFactory.databaseSpecificDistinct.get(databaseType));
//
//      properties.put("escapeChar", DbSqlSessionFactory.databaseSpecificEscapeChar.get(databaseType));
//
//      properties.put("bitand1", DbSqlSessionFactory.databaseSpecificBitAnd1.get(databaseType));
//      properties.put("bitand2", DbSqlSessionFactory.databaseSpecificBitAnd2.get(databaseType));
//      properties.put("bitand3", DbSqlSessionFactory.databaseSpecificBitAnd3.get(databaseType));
//
//      properties.put("datepart1", DbSqlSessionFactory.databaseSpecificDatepart1.get(databaseType));
//      properties.put("datepart2", DbSqlSessionFactory.databaseSpecificDatepart2.get(databaseType));
//      properties.put("datepart3", DbSqlSessionFactory.databaseSpecificDatepart3.get(databaseType));
//
//      properties.put("trueConstant", DbSqlSessionFactory.databaseSpecificTrueConstant.get(databaseType));
//      properties.put("falseConstant", DbSqlSessionFactory.databaseSpecificFalseConstant.get(databaseType));
//
//      properties.put("dbSpecificDummyTable", DbSqlSessionFactory.databaseSpecificDummyTable.get(databaseType));
//      properties.put("dbSpecificIfNullFunction", DbSqlSessionFactory.databaseSpecificIfNull.get(databaseType));
//
//      properties.put("dayComparator", DbSqlSessionFactory.databaseSpecificDaysComparator.get(databaseType));
//
//      Map constants = (Map) DbSqlSessionFactory.dbSpecificConstants.get(databaseType);
//
//      Iterator<Map.Entry<Integer, Integer>> it=constants.entrySet().iterator();
//      while(it.hasNext()) {
//        Map.Entry<Integer,Integer> entry=it.next();
//        properties.put(entry.getKey(), entry.getValue());
//      }
//
//    }
//  }
//
//  protected InputStream getMyBatisXmlConfigurationSteam()
//  {
//    return ReflectUtil.getResourceAsStream("org/camunda/bpm/engine/impl/mapping/mappings.xml");
//  }
//
//  protected void initIdentityProviderSessionFactory()
//  {
//    if (this.identityProviderSessionFactory == null)
//      this.identityProviderSessionFactory = new GenericManagerFactory(DbIdentityServiceProvider.class);
//  }
//
//  protected void initSessionFactories()
//  {
//    Class identityProviderType;
//    if (this.sessionFactories == null) {
//      this.sessionFactories = new HashMap();
//
//      initPersistenceProviders();
//
//      addSessionFactory(new DbEntityManagerFactory(this.idGenerator));
//
//      addSessionFactory(new GenericManagerFactory(AttachmentManager.class));
//      addSessionFactory(new GenericManagerFactory(CommentManager.class));
//      addSessionFactory(new GenericManagerFactory(DeploymentManager.class));
//      addSessionFactory(new GenericManagerFactory(ExecutionManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoricActivityInstanceManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoricCaseActivityInstanceManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoricStatisticsManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoricDetailManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoricProcessInstanceManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoricCaseInstanceManager.class));
//      addSessionFactory(new GenericManagerFactory(UserOperationLogManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoricTaskInstanceManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoricVariableInstanceManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoricIncidentManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoricIdentityLinkLogManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoricJobLogManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoricExternalTaskLogManager.class));
//      addSessionFactory(new GenericManagerFactory(IdentityInfoManager.class));
//      addSessionFactory(new GenericManagerFactory(IdentityLinkManager.class));
//      addSessionFactory(new GenericManagerFactory(JobManager.class));
//      addSessionFactory(new GenericManagerFactory(JobDefinitionManager.class));
//      addSessionFactory(new GenericManagerFactory(ProcessDefinitionManager.class));
//      addSessionFactory(new GenericManagerFactory(PropertyManager.class));
//      addSessionFactory(new GenericManagerFactory(ResourceManager.class));
//      addSessionFactory(new GenericManagerFactory(ByteArrayManager.class));
//      addSessionFactory(new GenericManagerFactory(TableDataManager.class));
//      addSessionFactory(new GenericManagerFactory(TaskManager.class));
//      addSessionFactory(new GenericManagerFactory(TaskReportManager.class));
//      addSessionFactory(new GenericManagerFactory(VariableInstanceManager.class));
//      addSessionFactory(new GenericManagerFactory(EventSubscriptionManager.class));
//      addSessionFactory(new GenericManagerFactory(StatisticsManager.class));
//      addSessionFactory(new GenericManagerFactory(IncidentManager.class));
//      addSessionFactory(new GenericManagerFactory(AuthorizationManager.class));
//      addSessionFactory(new GenericManagerFactory(FilterManager.class));
//      addSessionFactory(new GenericManagerFactory(MeterLogManager.class));
//      addSessionFactory(new GenericManagerFactory(ExternalTaskManager.class));
//      addSessionFactory(new GenericManagerFactory(ReportManager.class));
//      addSessionFactory(new GenericManagerFactory(BatchManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoricBatchManager.class));
//      addSessionFactory(new GenericManagerFactory(TenantManager.class));
//
//      addSessionFactory(new GenericManagerFactory(CaseDefinitionManager.class));
//      addSessionFactory(new GenericManagerFactory(CaseExecutionManager.class));
//      addSessionFactory(new GenericManagerFactory(CaseSentryPartManager.class));
//
//      addSessionFactory(new GenericManagerFactory(DecisionDefinitionManager.class));
//      addSessionFactory(new GenericManagerFactory(DecisionRequirementsDefinitionManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoricDecisionInstanceManager.class));
//
//      addSessionFactory(new GenericManagerFactory(OptimizeManager.class));
//
//      this.sessionFactories.put(ReadOnlyIdentityProvider.class, this.identityProviderSessionFactory);
//
//      identityProviderType = this.identityProviderSessionFactory.getSessionType();
//      if (WritableIdentityProvider.class.isAssignableFrom(identityProviderType)) {
//        this.sessionFactories.put(WritableIdentityProvider.class, this.identityProviderSessionFactory);
//      }
//    }
//
//    if (this.customSessionFactories != null)
//      for (SessionFactory sessionFactory : this.customSessionFactories)
//        addSessionFactory(sessionFactory);
//  }
//
//  protected void initPersistenceProviders()
//  {
//    ensurePrefixAndSchemaFitToegether(this.databaseTablePrefix, this.databaseSchema);
//    this.dbSqlSessionFactory = new DbSqlSessionFactory();
//    this.dbSqlSessionFactory.setDatabaseType(this.databaseType);
//    this.dbSqlSessionFactory.setIdGenerator(this.idGenerator);
//    this.dbSqlSessionFactory.setSqlSessionFactory(this.sqlSessionFactory);
//    this.dbSqlSessionFactory.setDbIdentityUsed(this.isDbIdentityUsed);
//    this.dbSqlSessionFactory.setDbHistoryUsed(this.isDbHistoryUsed);
//    this.dbSqlSessionFactory.setCmmnEnabled(this.cmmnEnabled);
//    this.dbSqlSessionFactory.setDmnEnabled(this.dmnEnabled);
//    this.dbSqlSessionFactory.setDatabaseTablePrefix(this.databaseTablePrefix);
//
//    if ((this.databaseTablePrefix != null) && (this.databaseSchema == null) && (this.databaseTablePrefix.contains("."))) {
//      this.databaseSchema = this.databaseTablePrefix.split("\\.")[0];
//    }
//    this.dbSqlSessionFactory.setDatabaseSchema(this.databaseSchema);
//    addSessionFactory(this.dbSqlSessionFactory);
//    addSessionFactory(new DbSqlPersistenceProviderFactory());
//  }
//
//  protected void initMigration() {
//    initMigrationInstructionValidators();
//    initMigrationActivityMatcher();
//    initMigrationInstructionGenerator();
//    initMigratingActivityInstanceValidators();
//    initMigratingTransitionInstanceValidators();
//    initMigratingCompensationInstanceValidators();
//  }
//
//  protected void initMigrationActivityMatcher() {
//    if (this.migrationActivityMatcher == null)
//      this.migrationActivityMatcher = new DefaultMigrationActivityMatcher();
//  }
//
//  protected void initMigrationInstructionGenerator()
//  {
//    if (this.migrationInstructionGenerator == null) {
//      this.migrationInstructionGenerator = new DefaultMigrationInstructionGenerator(this.migrationActivityMatcher);
//    }
//
//    List migrationActivityValidators = new ArrayList();
//    if (this.customPreMigrationActivityValidators != null) {
//      migrationActivityValidators.addAll(this.customPreMigrationActivityValidators);
//    }
//    migrationActivityValidators.addAll(getDefaultMigrationActivityValidators());
//    if (this.customPostMigrationActivityValidators != null) {
//      migrationActivityValidators.addAll(this.customPostMigrationActivityValidators);
//    }
//    this.migrationInstructionGenerator = this.migrationInstructionGenerator
//            .migrationActivityValidators(migrationActivityValidators)
//            .migrationInstructionValidators(this.migrationInstructionValidators);
//  }
//
//  protected void initMigrationInstructionValidators()
//  {
//    if (this.migrationInstructionValidators == null) {
//      this.migrationInstructionValidators = new ArrayList();
//      if (this.customPreMigrationInstructionValidators != null) {
//        this.migrationInstructionValidators.addAll(this.customPreMigrationInstructionValidators);
//      }
//      this.migrationInstructionValidators.addAll(getDefaultMigrationInstructionValidators());
//      if (this.customPostMigrationInstructionValidators != null)
//        this.migrationInstructionValidators.addAll(this.customPostMigrationInstructionValidators);
//    }
//  }
//
//  protected void initMigratingActivityInstanceValidators()
//  {
//    if (this.migratingActivityInstanceValidators == null) {
//      this.migratingActivityInstanceValidators = new ArrayList();
//      if (this.customPreMigratingActivityInstanceValidators != null) {
//        this.migratingActivityInstanceValidators.addAll(this.customPreMigratingActivityInstanceValidators);
//      }
//      this.migratingActivityInstanceValidators.addAll(getDefaultMigratingActivityInstanceValidators());
//      if (this.customPostMigratingActivityInstanceValidators != null)
//        this.migratingActivityInstanceValidators.addAll(this.customPostMigratingActivityInstanceValidators);
//    }
//  }
//
//  protected void initMigratingTransitionInstanceValidators()
//  {
//    if (this.migratingTransitionInstanceValidators == null) {
//      this.migratingTransitionInstanceValidators = new ArrayList();
//      this.migratingTransitionInstanceValidators.addAll(getDefaultMigratingTransitionInstanceValidators());
//    }
//  }
//
//  protected void initMigratingCompensationInstanceValidators() {
//    if (this.migratingCompensationInstanceValidators == null) {
//      this.migratingCompensationInstanceValidators = new ArrayList();
//
//      this.migratingCompensationInstanceValidators.add(new NoUnmappedLeafInstanceValidator());
//      this.migratingCompensationInstanceValidators.add(new NoUnmappedCompensationStartEventValidator());
//    }
//  }
//
//  protected void ensurePrefixAndSchemaFitToegether(String prefix, String schema)
//  {
//    if (schema == null)
//      return;
//    if ((prefix == null) || ((prefix != null) && (!prefix.startsWith(schema + "."))))
//      throw new ProcessEngineException("When setting a schema the prefix has to be schema + '.'. Received schema: " + schema + " prefix: " + prefix);
//  }
//
//  protected void addSessionFactory(SessionFactory sessionFactory)
//  {
//    this.sessionFactories.put(sessionFactory.getSessionType(), sessionFactory);
//  }
//
//  protected void initDeployers()
//  {
//    if (this.deployers == null) {
//      this.deployers = new ArrayList();
//      if (this.customPreDeployers != null) {
//        this.deployers.addAll(this.customPreDeployers);
//      }
//      this.deployers.addAll(getDefaultDeployers());
//      if (this.customPostDeployers != null) {
//        this.deployers.addAll(this.customPostDeployers);
//      }
//    }
//    if (this.deploymentCache == null) {
//      List deployers = new ArrayList();
//      if (this.customPreDeployers != null) {
//        deployers.addAll(this.customPreDeployers);
//      }
//      deployers.addAll(getDefaultDeployers());
//      if (this.customPostDeployers != null) {
//        deployers.addAll(this.customPostDeployers);
//      }
//
//      initCacheFactory();
//      this.deploymentCache = new DeploymentCache(this.cacheFactory, this.cacheCapacity);
//      this.deploymentCache.setDeployers(deployers);
//    }
//  }
//
//  protected Collection<? extends Deployer> getDefaultDeployers() {
//    List defaultDeployers = new ArrayList();
//
//    BpmnDeployer bpmnDeployer = getBpmnDeployer();
//    defaultDeployers.add(bpmnDeployer);
//
//    if (isCmmnEnabled()) {
//      CmmnDeployer cmmnDeployer = getCmmnDeployer();
//      defaultDeployers.add(cmmnDeployer);
//    }
//
//    if (isDmnEnabled()) {
//      DecisionRequirementsDefinitionDeployer decisionRequirementsDefinitionDeployer = getDecisionRequirementsDefinitionDeployer();
//      DecisionDefinitionDeployer decisionDefinitionDeployer = getDecisionDefinitionDeployer();
//
//      defaultDeployers.add(decisionRequirementsDefinitionDeployer);
//      defaultDeployers.add(decisionDefinitionDeployer);
//    }
//
//    return defaultDeployers;
//  }
//
//  protected BpmnDeployer getBpmnDeployer() {
//    BpmnDeployer bpmnDeployer = new BpmnDeployer();
//    bpmnDeployer.setExpressionManager(this.expressionManager);
//    bpmnDeployer.setIdGenerator(this.idGenerator);
//
//    if (this.bpmnParseFactory == null) {
//      this.bpmnParseFactory = new DefaultBpmnParseFactory();
//    }
//
//    BpmnParser bpmnParser = new BpmnParser(this.expressionManager, this.bpmnParseFactory);
//
//    if (this.preParseListeners != null) {
//      bpmnParser.getParseListeners().addAll(this.preParseListeners);
//    }
//    bpmnParser.getParseListeners().addAll(getDefaultBPMNParseListeners());
//    if (this.postParseListeners != null) {
//      bpmnParser.getParseListeners().addAll(this.postParseListeners);
//    }
//
//    bpmnDeployer.setBpmnParser(bpmnParser);
//
//    return bpmnDeployer;
//  }
//
//  protected List<BpmnParseListener> getDefaultBPMNParseListeners() {
//    List defaultListeners = new ArrayList();
//    if (!HistoryLevel.HISTORY_LEVEL_NONE.equals(this.historyLevel)) {
//      defaultListeners.add(new HistoryParseListener(this.historyLevel, this.historyEventProducer));
//    }
//    if (this.isMetricsEnabled) {
//      defaultListeners.add(new MetricsBpmnParseListener());
//    }
//    return defaultListeners;
//  }
//
//  protected CmmnDeployer getCmmnDeployer() {
//    CmmnDeployer cmmnDeployer = new CmmnDeployer();
//
//    cmmnDeployer.setIdGenerator(this.idGenerator);
//
//    if (this.cmmnTransformFactory == null) {
//      this.cmmnTransformFactory = new DefaultCmmnTransformFactory();
//    }
//
//    if (this.cmmnElementHandlerRegistry == null) {
//      this.cmmnElementHandlerRegistry = new DefaultCmmnElementHandlerRegistry();
//    }
//
//    CmmnTransformer cmmnTransformer = new CmmnTransformer(this.expressionManager, this.cmmnElementHandlerRegistry, this.cmmnTransformFactory);
//
//    if (this.customPreCmmnTransformListeners != null) {
//      cmmnTransformer.getTransformListeners().addAll(this.customPreCmmnTransformListeners);
//    }
//    cmmnTransformer.getTransformListeners().addAll(getDefaultCmmnTransformListeners());
//    if (this.customPostCmmnTransformListeners != null) {
//      cmmnTransformer.getTransformListeners().addAll(this.customPostCmmnTransformListeners);
//    }
//
//    cmmnDeployer.setTransformer(cmmnTransformer);
//
//    return cmmnDeployer;
//  }
//
//  protected List<CmmnTransformListener> getDefaultCmmnTransformListeners() {
//    List defaultListener = new ArrayList();
//    if (!HistoryLevel.HISTORY_LEVEL_NONE.equals(this.historyLevel)) {
//      defaultListener.add(new CmmnHistoryTransformListener(this.historyLevel, this.cmmnHistoryEventProducer));
//    }
//    if (this.isMetricsEnabled) {
//      defaultListener.add(new MetricsCmmnTransformListener());
//    }
//    return defaultListener;
//  }
//
//  protected DecisionDefinitionDeployer getDecisionDefinitionDeployer() {
//    DecisionDefinitionDeployer decisionDefinitionDeployer = new DecisionDefinitionDeployer();
//    decisionDefinitionDeployer.setIdGenerator(this.idGenerator);
//    decisionDefinitionDeployer.setTransformer(this.dmnEngineConfiguration.getTransformer());
//    return decisionDefinitionDeployer;
//  }
//
//  protected DecisionRequirementsDefinitionDeployer getDecisionRequirementsDefinitionDeployer() {
//    DecisionRequirementsDefinitionDeployer drdDeployer = new DecisionRequirementsDefinitionDeployer();
//    drdDeployer.setIdGenerator(this.idGenerator);
//    drdDeployer.setTransformer(this.dmnEngineConfiguration.getTransformer());
//    return drdDeployer;
//  }
//
//  public DmnEngine getDmnEngine() {
//    return this.dmnEngine;
//  }
//
//  public void setDmnEngine(DmnEngine dmnEngine) {
//    this.dmnEngine = dmnEngine;
//  }
//
//  public DefaultDmnEngineConfiguration getDmnEngineConfiguration() {
//    return this.dmnEngineConfiguration;
//  }
//
//  public void setDmnEngineConfiguration(DefaultDmnEngineConfiguration dmnEngineConfiguration) {
//    this.dmnEngineConfiguration = dmnEngineConfiguration;
//  }
//
//  protected void initJobExecutor()
//  {
//    if (this.jobExecutor == null) {
//      this.jobExecutor = new DefaultJobExecutor();
//    }
//
//    this.jobHandlers = new HashMap();
//    TimerExecuteNestedActivityJobHandler timerExecuteNestedActivityJobHandler = new TimerExecuteNestedActivityJobHandler();
//    this.jobHandlers.put(timerExecuteNestedActivityJobHandler.getType(), timerExecuteNestedActivityJobHandler);
//
//    TimerCatchIntermediateEventJobHandler timerCatchIntermediateEvent = new TimerCatchIntermediateEventJobHandler();
//    this.jobHandlers.put(timerCatchIntermediateEvent.getType(), timerCatchIntermediateEvent);
//
//    TimerStartEventJobHandler timerStartEvent = new TimerStartEventJobHandler();
//    this.jobHandlers.put(timerStartEvent.getType(), timerStartEvent);
//
//    TimerStartEventSubprocessJobHandler timerStartEventSubprocess = new TimerStartEventSubprocessJobHandler();
//    this.jobHandlers.put(timerStartEventSubprocess.getType(), timerStartEventSubprocess);
//
//    AsyncContinuationJobHandler asyncContinuationJobHandler = new AsyncContinuationJobHandler();
//    this.jobHandlers.put(asyncContinuationJobHandler.getType(), asyncContinuationJobHandler);
//
//    ProcessEventJobHandler processEventJobHandler = new ProcessEventJobHandler();
//    this.jobHandlers.put(processEventJobHandler.getType(), processEventJobHandler);
//
//    TimerSuspendProcessDefinitionHandler suspendProcessDefinitionHandler = new TimerSuspendProcessDefinitionHandler();
//    this.jobHandlers.put(suspendProcessDefinitionHandler.getType(), suspendProcessDefinitionHandler);
//
//    TimerActivateProcessDefinitionHandler activateProcessDefinitionHandler = new TimerActivateProcessDefinitionHandler();
//    this.jobHandlers.put(activateProcessDefinitionHandler.getType(), activateProcessDefinitionHandler);
//
//    TimerSuspendJobDefinitionHandler suspendJobDefinitionHandler = new TimerSuspendJobDefinitionHandler();
//    this.jobHandlers.put(suspendJobDefinitionHandler.getType(), suspendJobDefinitionHandler);
//
//    TimerActivateJobDefinitionHandler activateJobDefinitionHandler = new TimerActivateJobDefinitionHandler();
//    this.jobHandlers.put(activateJobDefinitionHandler.getType(), activateJobDefinitionHandler);
//
//    BatchSeedJobHandler batchSeedJobHandler = new BatchSeedJobHandler();
//    this.jobHandlers.put(batchSeedJobHandler.getType(), batchSeedJobHandler);
//
//    BatchMonitorJobHandler batchMonitorJobHandler = new BatchMonitorJobHandler();
//    this.jobHandlers.put(batchMonitorJobHandler.getType(), batchMonitorJobHandler);
//
//    HistoryCleanupJobHandler historyCleanupJobHandler = new HistoryCleanupJobHandler();
//    this.jobHandlers.put(historyCleanupJobHandler.getType(), historyCleanupJobHandler);
//
//    for (JobHandler batchHandler : this.batchHandlers.values()) {
//      this.jobHandlers.put(batchHandler.getType(), batchHandler);
//    }
//
//    if (getCustomJobHandlers() != null) {
//      for (JobHandler customJobHandler : getCustomJobHandlers()) {
//        this.jobHandlers.put(customJobHandler.getType(), customJobHandler);
//      }
//    }
//
//    this.jobExecutor.setAutoActivate(this.jobExecutorActivate);
//
//    if (this.jobExecutor.getRejectedJobsHandler() == null)
//      if (this.customRejectedJobsHandler != null)
//        this.jobExecutor.setRejectedJobsHandler(this.customRejectedJobsHandler);
//      else
//        this.jobExecutor.setRejectedJobsHandler(new NotifyAcquisitionRejectedJobsHandler());
//  }
//
//  protected void initJobProvider()
//  {
//    if ((this.producePrioritizedJobs) && (this.jobPriorityProvider == null))
//      this.jobPriorityProvider = new DefaultJobPriorityProvider();
//  }
//
//  protected void initExternalTaskPriorityProvider()
//  {
//    if ((this.producePrioritizedExternalTasks) && (this.externalTaskPriorityProvider == null))
//      this.externalTaskPriorityProvider = new DefaultExternalTaskPriorityProvider();
//  }
//
//  public void initHistoryLevel()
//  {
//    if (this.historyLevel != null) {
//      setHistory(this.historyLevel.getName());
//    }
//
//    if (this.historyLevels == null) {
//      this.historyLevels = new ArrayList();
//      this.historyLevels.add(HistoryLevel.HISTORY_LEVEL_NONE);
//      this.historyLevels.add(HistoryLevel.HISTORY_LEVEL_ACTIVITY);
//      this.historyLevels.add(HistoryLevel.HISTORY_LEVEL_AUDIT);
//      this.historyLevels.add(HistoryLevel.HISTORY_LEVEL_FULL);
//    }
//
//    if (this.customHistoryLevels != null) {
//      this.historyLevels.addAll(this.customHistoryLevels);
//    }
//
//    if ("variable".equalsIgnoreCase(this.history)) {
//      this.historyLevel = HistoryLevel.HISTORY_LEVEL_ACTIVITY;
//      LOG.usingDeprecatedHistoryLevelVariable();
//    } else {
//      for (HistoryLevel historyLevel : this.historyLevels) {
//        if (historyLevel.getName().equalsIgnoreCase(this.history)) {
//          this.historyLevel = historyLevel;
//        }
//      }
//
//    }
//
//    if ((this.historyLevel == null) && (!"auto".equalsIgnoreCase(this.history)))
//      throw new ProcessEngineException("invalid history level: " + this.history);
//  }
//
//  protected void initIdGenerator()
//  {
//    if (this.idGenerator == null) {
//      CommandExecutor idGeneratorCommandExecutor = null;
//      if (this.idGeneratorDataSource != null) {
//        ProcessEngineConfigurationImpl processEngineConfiguration = new StandaloneProcessEngineConfiguration();
//        processEngineConfiguration.setDataSource(this.idGeneratorDataSource);
//        processEngineConfiguration.setDatabaseSchemaUpdate("false");
//        processEngineConfiguration.init();
//        idGeneratorCommandExecutor = processEngineConfiguration.getCommandExecutorTxRequiresNew();
//      } else if (this.idGeneratorDataSourceJndiName != null) {
//        ProcessEngineConfigurationImpl processEngineConfiguration = new StandaloneProcessEngineConfiguration();
//        processEngineConfiguration.setDataSourceJndiName(this.idGeneratorDataSourceJndiName);
//        processEngineConfiguration.setDatabaseSchemaUpdate("false");
//        processEngineConfiguration.init();
//        idGeneratorCommandExecutor = processEngineConfiguration.getCommandExecutorTxRequiresNew();
//      } else {
//        idGeneratorCommandExecutor = this.commandExecutorTxRequiresNew;
//      }
//
//      DbIdGenerator dbIdGenerator = new DbIdGenerator();
//      dbIdGenerator.setIdBlockSize(this.idBlockSize);
//      dbIdGenerator.setCommandExecutor(idGeneratorCommandExecutor);
//      this.idGenerator = dbIdGenerator;
//    }
//  }
//
//  protected void initCommandContextFactory()
//  {
//    if (this.commandContextFactory == null) {
//      this.commandContextFactory = new CommandContextFactory();
//      this.commandContextFactory.setProcessEngineConfiguration(this);
//    }
//  }
//
//  protected void initTransactionContextFactory() {
//    if (this.transactionContextFactory == null)
//      this.transactionContextFactory = new StandaloneTransactionContextFactory();
//  }
//
//  protected void initValueTypeResolver()
//  {
//    if (this.valueTypeResolver == null)
//      this.valueTypeResolver = new ValueTypeResolverImpl();
//  }
//
//  protected void initDefaultCharset()
//  {
//    if (this.defaultCharset == null) {
//      if (this.defaultCharsetName == null) {
//        this.defaultCharsetName = "UTF-8";
//      }
//      this.defaultCharset = Charset.forName(this.defaultCharsetName);
//    }
//  }
//
//  protected void initMetrics() {
//    if (this.isMetricsEnabled)
//    {
//      if (this.metricsReporterIdProvider == null) {
//        this.metricsReporterIdProvider = new SimpleIpBasedProvider();
//      }
//
//      if (this.metricsRegistry == null) {
//        this.metricsRegistry = new MetricsRegistry();
//      }
//
//      initDefaultMetrics(this.metricsRegistry);
//
//      if (this.dbMetricsReporter == null)
//        this.dbMetricsReporter = new DbMetricsReporter(this.metricsRegistry, this.commandExecutorTxRequired);
//    }
//  }
//
//  protected void initDefaultMetrics(MetricsRegistry metricsRegistry)
//  {
//    metricsRegistry.createMeter("activity-instance-start");
//    metricsRegistry.createMeter("activity-instance-end");
//
//    metricsRegistry.createMeter("job-acquisition-attempt");
//    metricsRegistry.createMeter("job-acquired-success");
//    metricsRegistry.createMeter("job-acquired-failure");
//    metricsRegistry.createMeter("job-successful");
//    metricsRegistry.createMeter("job-failed");
//    metricsRegistry.createMeter("job-locked-exclusive");
//    metricsRegistry.createMeter("job-execution-rejected");
//
//    metricsRegistry.createMeter("executed-decision-elements");
//  }
//
//  protected void initSerialization() {
//    if (this.variableSerializers == null) {
//      this.variableSerializers = new DefaultVariableSerializers();
//
//      if (this.customPreVariableSerializers != null) {
//        for (TypedValueSerializer customVariableType : this.customPreVariableSerializers) {
//          this.variableSerializers.addSerializer(customVariableType);
//        }
//
//      }
//
//      this.variableSerializers.addSerializer(new NullValueSerializer());
//      this.variableSerializers.addSerializer(new StringValueSerializer());
//      this.variableSerializers.addSerializer(new BooleanValueSerializer());
//      this.variableSerializers.addSerializer(new ShortValueSerializer());
//      this.variableSerializers.addSerializer(new IntegerValueSerializer());
//      this.variableSerializers.addSerializer(new LongValueSerlializer());
//      this.variableSerializers.addSerializer(new DateValueSerializer());
//      this.variableSerializers.addSerializer(new DoubleValueSerializer());
//      this.variableSerializers.addSerializer(new ByteArrayValueSerializer());
//      this.variableSerializers.addSerializer(new JavaObjectSerializer());
//      this.variableSerializers.addSerializer(new FileValueSerializer());
//
//      if (this.customPostVariableSerializers != null)
//        for (TypedValueSerializer customVariableType : this.customPostVariableSerializers)
//          this.variableSerializers.addSerializer(customVariableType);
//    }
//  }
//
//  protected void initFormEngines()
//  {
//    FormEngine defaultFormEngine;
//    if (this.formEngines == null) {
//      this.formEngines = new HashMap();
//
//      defaultFormEngine = new HtmlFormEngine();
//      this.formEngines.put(null, defaultFormEngine);
//      this.formEngines.put(defaultFormEngine.getName(), defaultFormEngine);
//      FormEngine juelFormEngine = new JuelFormEngine();
//      this.formEngines.put(juelFormEngine.getName(), juelFormEngine);
//    }
//
//    if (this.customFormEngines != null)
//      for (FormEngine formEngine : this.customFormEngines)
//        this.formEngines.put(formEngine.getName(), formEngine);
//  }
//
//  protected void initFormTypes()
//  {
//    if (this.formTypes == null) {
//      this.formTypes = new FormTypes();
//      this.formTypes.addFormType(new StringFormType());
//      this.formTypes.addFormType(new LongFormType());
//      this.formTypes.addFormType(new DateFormType("dd/MM/yyyy"));
//      this.formTypes.addFormType(new BooleanFormType());
//    }
//    if (this.customFormTypes != null)
//      for (AbstractFormFieldType customFormType : this.customFormTypes)
//        this.formTypes.addFormType(customFormType);
//  }
//
//  protected void initFormFieldValidators()
//  {
//    if (this.formValidators == null) {
//      this.formValidators = new FormValidators();
//      this.formValidators.addValidator("min", MinValidator.class);
//      this.formValidators.addValidator("max", MaxValidator.class);
//      this.formValidators.addValidator("minlength", MinLengthValidator.class);
//      this.formValidators.addValidator("maxlength", MaxLengthValidator.class);
//      this.formValidators.addValidator("required", RequiredValidator.class);
//      this.formValidators.addValidator("readonly", ReadOnlyValidator.class);
//    }
//    if (this.customFormFieldValidators != null)
//      for (Map.Entry validator : this.customFormFieldValidators.entrySet())
//        this.formValidators.addValidator((String)validator.getKey(), (Class)validator.getValue());
//  }
//
//  protected void initScripting()
//  {
//    if (this.resolverFactories == null) {
//      this.resolverFactories = new ArrayList();
//      this.resolverFactories.add(new MocksResolverFactory());
//      this.resolverFactories.add(new VariableScopeResolverFactory());
//      this.resolverFactories.add(new BeansResolverFactory());
//    }
//    if (this.scriptingEngines == null) {
//      this.scriptingEngines = new ScriptingEngines(new ScriptBindingsFactory(this.resolverFactories));
//      this.scriptingEngines.setEnableScriptEngineCaching(this.enableScriptEngineCaching);
//    }
//    if (this.scriptFactory == null) {
//      this.scriptFactory = new ScriptFactory();
//    }
//    if (this.scriptEnvResolvers == null) {
//      this.scriptEnvResolvers = new ArrayList();
//    }
//    if (this.scriptingEnvironment == null)
//      this.scriptingEnvironment = new ScriptingEnvironment(this.scriptFactory, this.scriptEnvResolvers, this.scriptingEngines);
//  }
//
//  protected void initDmnEngine()
//  {
//    if (this.dmnEngine == null)
//    {
//      if (this.dmnEngineConfiguration == null) {
//        this.dmnEngineConfiguration = ((DefaultDmnEngineConfiguration) DmnEngineConfiguration.createDefaultDmnEngineConfiguration());
//      }
//
//      this.dmnEngineConfiguration = new DmnEngineConfigurationBuilder(this.dmnEngineConfiguration)
//              .historyLevel(this.historyLevel)
//              .dmnHistoryEventProducer(this.dmnHistoryEventProducer)
//              .scriptEngineResolver(this.scriptingEngines)
//              .expressionManager(this.expressionManager)
//              .build();
//
//      this.dmnEngine = this.dmnEngineConfiguration.buildEngine();
//    }
//    else if (this.dmnEngineConfiguration == null) {
//      this.dmnEngineConfiguration = ((DefaultDmnEngineConfiguration)this.dmnEngine.getConfiguration());
//    }
//  }
//
//  protected void initExpressionManager() {
//    if (this.expressionManager == null) {
//      this.expressionManager = new ExpressionManager(this.beans);
//    }
//
//    this.expressionManager.addFunctionMapper(new CommandContextFunctionMapper());
//
//    this.expressionManager.addFunctionMapper(new DateTimeFunctionMapper());
//  }
//
//  protected void initBusinessCalendarManager() {
//    if (this.businessCalendarManager == null) {
//      MapBusinessCalendarManager mapBusinessCalendarManager = new MapBusinessCalendarManager();
//      mapBusinessCalendarManager.addBusinessCalendar(DurationBusinessCalendar.NAME, new DurationBusinessCalendar());
//      mapBusinessCalendarManager.addBusinessCalendar("dueDate", new DueDateBusinessCalendar());
//      mapBusinessCalendarManager.addBusinessCalendar(CycleBusinessCalendar.NAME, new CycleBusinessCalendar());
//
//      this.businessCalendarManager = mapBusinessCalendarManager;
//    }
//  }
//
//  protected void initDelegateInterceptor() {
//    if (this.delegateInterceptor == null)
//      this.delegateInterceptor = new DefaultDelegateInterceptor();
//  }
//
//  protected void initEventHandlers()
//  {
//    SignalEventHandler signalEventHander;
//    if (this.eventHandlers == null) {
//      this.eventHandlers = new HashMap();
//
//      signalEventHander = new SignalEventHandler();
//      this.eventHandlers.put(signalEventHander.getEventHandlerType(), signalEventHander);
//
//      CompensationEventHandler compensationEventHandler = new CompensationEventHandler();
//      this.eventHandlers.put(compensationEventHandler.getEventHandlerType(), compensationEventHandler);
//
//      EventHandler messageEventHandler = new EventHandlerImpl(EventType.MESSAGE);
//      this.eventHandlers.put(messageEventHandler.getEventHandlerType(), messageEventHandler);
//
//      EventHandler conditionalEventHandler = new ConditionalEventHandler();
//      this.eventHandlers.put(conditionalEventHandler.getEventHandlerType(), conditionalEventHandler);
//    }
//
//    if (this.customEventHandlers != null)
//      for (EventHandler eventHandler : this.customEventHandlers)
//        this.eventHandlers.put(eventHandler.getEventHandlerType(), eventHandler);
//  }
//
//  protected void initCommandCheckers()
//  {
//    if (this.commandCheckers == null) {
//      this.commandCheckers = new ArrayList();
//
//      this.commandCheckers.add(new TenantCommandChecker());
//      this.commandCheckers.add(new AuthorizationCommandChecker());
//    }
//  }
//
//  protected void initJpa()
//  {
//    if (this.jpaPersistenceUnitName != null) {
//      this.jpaEntityManagerFactory = JpaHelper.createEntityManagerFactory(this.jpaPersistenceUnitName);
//
//    }
//    if (this.jpaEntityManagerFactory != null) {
//      this.sessionFactories.put(EntityManagerSession.class, new EntityManagerSessionFactory(this.jpaEntityManagerFactory, this.jpaHandleTransaction, this.jpaCloseEntityManager));
//      JPAVariableSerializer jpaType = (JPAVariableSerializer)this.variableSerializers.getSerializerByName("jpa");
//
//      if (jpaType == null)
//      {
//        int serializableIndex = this.variableSerializers.getSerializerIndexByName(ValueType.BYTES.getName());
//        if (serializableIndex > -1)
//          this.variableSerializers.addSerializer(new JPAVariableSerializer(), serializableIndex);
//        else
//          this.variableSerializers.addSerializer(new JPAVariableSerializer());
//      }
//    }
//  }
//
//  protected void initBeans()
//  {
//    if (this.beans == null)
//      this.beans = new HashMap();
//  }
//
//  protected void initArtifactFactory()
//  {
//    if (this.artifactFactory == null)
//      this.artifactFactory = new DefaultArtifactFactory();
//  }
//
//  protected void initProcessApplicationManager()
//  {
//    if (this.processApplicationManager == null)
//      this.processApplicationManager = new ProcessApplicationManager();
//  }
//
//  protected void initCorrelationHandler()
//  {
//    if (this.correlationHandler == null)
//      this.correlationHandler = new DefaultCorrelationHandler();
//  }
//
//  protected void initConditionHandler()
//  {
//    if (this.conditionHandler == null)
//      this.conditionHandler = new DefaultConditionHandler();
//  }
//
//  protected void initHistoryEventProducer()
//  {
//    if (this.historyEventProducer == null)
//      this.historyEventProducer = new CacheAwareHistoryEventProducer();
//  }
//
//  protected void initCmmnHistoryEventProducer()
//  {
//    if (this.cmmnHistoryEventProducer == null)
//      this.cmmnHistoryEventProducer = new CacheAwareCmmnHistoryEventProducer();
//  }
//
//  protected void initDmnHistoryEventProducer()
//  {
//    if (this.dmnHistoryEventProducer == null)
//      this.dmnHistoryEventProducer = new DefaultDmnHistoryEventProducer();
//  }
//
//  protected void initHistoryEventHandler()
//  {
//    if (this.historyEventHandler == null)
//      this.historyEventHandler = new DbHistoryEventHandler();
//  }
//
//  protected void initPasswordDigest()
//  {
//    if (this.saltGenerator == null) {
//      this.saltGenerator = new Default16ByteSaltGenerator();
//    }
//    if (this.passwordEncryptor == null) {
//      this.passwordEncryptor = new Sha512HashDigest();
//    }
//    if (this.customPasswordChecker == null) {
//      this.customPasswordChecker = Collections.emptyList();
//    }
//    if (this.passwordManager == null)
//      this.passwordManager = new PasswordManager(this.passwordEncryptor, this.customPasswordChecker);
//  }
//
//  protected void initDeploymentRegistration()
//  {
//    if (this.registeredDeployments == null)
//      this.registeredDeployments = new CopyOnWriteArraySet();
//  }
//
//  protected void initCacheFactory()
//  {
//    if (this.cacheFactory == null)
//      this.cacheFactory = new DefaultCacheFactory();
//  }
//
//  protected void initResourceAuthorizationProvider()
//  {
//    if (this.resourceAuthorizationProvider == null)
//      this.resourceAuthorizationProvider = new DefaultAuthorizationProvider();
//  }
//
//  protected void initDefaultUserPermissionForTask()
//  {
//    if (this.defaultUserPermissionForTask == null)
//      if (Permissions.UPDATE.getName().equals(this.defaultUserPermissionNameForTask))
//        this.defaultUserPermissionForTask = Permissions.UPDATE;
//      else if (Permissions.TASK_WORK.getName().equals(this.defaultUserPermissionNameForTask))
//        this.defaultUserPermissionForTask = Permissions.TASK_WORK;
//      else
//        throw LOG.invalidConfigDefaultUserPermissionNameForTask(this.defaultUserPermissionNameForTask, new String[] { Permissions.UPDATE.getName(), Permissions.TASK_WORK.getName() });
//  }
//
//  protected void initAdminUser()
//  {
//    if (this.adminUsers == null)
//      this.adminUsers = new ArrayList();
//  }
//
//  protected void initAdminGroups()
//  {
//    if (this.adminGroups == null) {
//      this.adminGroups = new ArrayList();
//    }
//    if ((this.adminGroups.isEmpty()) || (!this.adminGroups.contains("camunda-admin")))
//      this.adminGroups.add("camunda-admin");
//  }
//
//  public String getProcessEngineName()
//  {
//    return this.processEngineName;
//  }
//
//  public HistoryLevel getHistoryLevel() {
//    return this.historyLevel;
//  }
//
//  public void setHistoryLevel(HistoryLevel historyLevel) {
//    this.historyLevel = historyLevel;
//  }
//
//  public HistoryLevel getDefaultHistoryLevel() {
//    if (this.historyLevels != null) {
//      for (HistoryLevel historyLevel : this.historyLevels) {
//        if (("audit" != null) && ("audit".equalsIgnoreCase(historyLevel.getName()))) {
//          return historyLevel;
//        }
//      }
//    }
//
//    return null;
//  }
//
//  public ProcessEngineConfigurationImpl setProcessEngineName(String processEngineName)
//  {
//    this.processEngineName = processEngineName;
//    return this;
//  }
//
//  public List<CommandInterceptor> getCustomPreCommandInterceptorsTxRequired() {
//    return this.customPreCommandInterceptorsTxRequired;
//  }
//
//  public ProcessEngineConfigurationImpl setCustomPreCommandInterceptorsTxRequired(List<CommandInterceptor> customPreCommandInterceptorsTxRequired) {
//    this.customPreCommandInterceptorsTxRequired = customPreCommandInterceptorsTxRequired;
//    return this;
//  }
//
//  public List<CommandInterceptor> getCustomPostCommandInterceptorsTxRequired() {
//    return this.customPostCommandInterceptorsTxRequired;
//  }
//
//  public ProcessEngineConfigurationImpl setCustomPostCommandInterceptorsTxRequired(List<CommandInterceptor> customPostCommandInterceptorsTxRequired) {
//    this.customPostCommandInterceptorsTxRequired = customPostCommandInterceptorsTxRequired;
//    return this;
//  }
//
//  public List<CommandInterceptor> getCommandInterceptorsTxRequired() {
//    return this.commandInterceptorsTxRequired;
//  }
//
//  public ProcessEngineConfigurationImpl setCommandInterceptorsTxRequired(List<CommandInterceptor> commandInterceptorsTxRequired) {
//    this.commandInterceptorsTxRequired = commandInterceptorsTxRequired;
//    return this;
//  }
//
//  public CommandExecutor getCommandExecutorTxRequired() {
//    return this.commandExecutorTxRequired;
//  }
//
//  public ProcessEngineConfigurationImpl setCommandExecutorTxRequired(CommandExecutor commandExecutorTxRequired) {
//    this.commandExecutorTxRequired = commandExecutorTxRequired;
//    return this;
//  }
//
//  public List<CommandInterceptor> getCustomPreCommandInterceptorsTxRequiresNew() {
//    return this.customPreCommandInterceptorsTxRequiresNew;
//  }
//
//  public ProcessEngineConfigurationImpl setCustomPreCommandInterceptorsTxRequiresNew(List<CommandInterceptor> customPreCommandInterceptorsTxRequiresNew) {
//    this.customPreCommandInterceptorsTxRequiresNew = customPreCommandInterceptorsTxRequiresNew;
//    return this;
//  }
//
//  public List<CommandInterceptor> getCustomPostCommandInterceptorsTxRequiresNew() {
//    return this.customPostCommandInterceptorsTxRequiresNew;
//  }
//
//  public ProcessEngineConfigurationImpl setCustomPostCommandInterceptorsTxRequiresNew(List<CommandInterceptor> customPostCommandInterceptorsTxRequiresNew) {
//    this.customPostCommandInterceptorsTxRequiresNew = customPostCommandInterceptorsTxRequiresNew;
//    return this;
//  }
//
//  public List<CommandInterceptor> getCommandInterceptorsTxRequiresNew() {
//    return this.commandInterceptorsTxRequiresNew;
//  }
//
//  public ProcessEngineConfigurationImpl setCommandInterceptorsTxRequiresNew(List<CommandInterceptor> commandInterceptorsTxRequiresNew) {
//    this.commandInterceptorsTxRequiresNew = commandInterceptorsTxRequiresNew;
//    return this;
//  }
//
//  public CommandExecutor getCommandExecutorTxRequiresNew() {
//    return this.commandExecutorTxRequiresNew;
//  }
//
//  public ProcessEngineConfigurationImpl setCommandExecutorTxRequiresNew(CommandExecutor commandExecutorTxRequiresNew) {
//    this.commandExecutorTxRequiresNew = commandExecutorTxRequiresNew;
//    return this;
//  }
//
//  public RepositoryService getRepositoryService() {
//    return this.repositoryService;
//  }
//
//  public ProcessEngineConfigurationImpl setRepositoryService(RepositoryService repositoryService) {
//    this.repositoryService = repositoryService;
//    return this;
//  }
//
//  public RuntimeService getRuntimeService() {
//    return this.runtimeService;
//  }
//
//  public ProcessEngineConfigurationImpl setRuntimeService(RuntimeService runtimeService) {
//    this.runtimeService = runtimeService;
//    return this;
//  }
//
//  public HistoryService getHistoryService() {
//    return this.historyService;
//  }
//
//  public ProcessEngineConfigurationImpl setHistoryService(HistoryService historyService) {
//    this.historyService = historyService;
//    return this;
//  }
//
//  public IdentityService getIdentityService() {
//    return this.identityService;
//  }
//
//  public ProcessEngineConfigurationImpl setIdentityService(IdentityService identityService) {
//    this.identityService = identityService;
//    return this;
//  }
//
//  public TaskService getTaskService() {
//    return this.taskService;
//  }
//
//  public ProcessEngineConfigurationImpl setTaskService(TaskService taskService) {
//    this.taskService = taskService;
//    return this;
//  }
//
//  public FormService getFormService() {
//    return this.formService;
//  }
//
//  public ProcessEngineConfigurationImpl setFormService(FormService formService) {
//    this.formService = formService;
//    return this;
//  }
//
//  public ManagementService getManagementService() {
//    return this.managementService;
//  }
//
//  public AuthorizationService getAuthorizationService() {
//    return this.authorizationService;
//  }
//
//  public ProcessEngineConfigurationImpl setManagementService(ManagementService managementService) {
//    this.managementService = managementService;
//    return this;
//  }
//
//  public CaseService getCaseService() {
//    return this.caseService;
//  }
//
//  public void setCaseService(CaseService caseService) {
//    this.caseService = caseService;
//  }
//
//  public FilterService getFilterService() {
//    return this.filterService;
//  }
//
//  public void setFilterService(FilterService filterService) {
//    this.filterService = filterService;
//  }
//
//  public ExternalTaskService getExternalTaskService() {
//    return this.externalTaskService;
//  }
//
//  public void setExternalTaskService(ExternalTaskService externalTaskService) {
//    this.externalTaskService = externalTaskService;
//  }
//
//  public DecisionService getDecisionService() {
//    return this.decisionService;
//  }
//
//  public OptimizeService getOptimizeService() {
//    return this.optimizeService;
//  }
//
//  public void setDecisionService(DecisionService decisionService) {
//    this.decisionService = decisionService;
//  }
//
//  public Map<Class<?>, SessionFactory> getSessionFactories() {
//    return this.sessionFactories;
//  }
//
//  public ProcessEngineConfigurationImpl setSessionFactories(Map<Class<?>, SessionFactory> sessionFactories) {
//    this.sessionFactories = sessionFactories;
//    return this;
//  }
//
//  public List<Deployer> getDeployers() {
//    return this.deployers;
//  }
//
//  public ProcessEngineConfigurationImpl setDeployers(List<Deployer> deployers) {
//    this.deployers = deployers;
//    return this;
//  }
//
//  public JobExecutor getJobExecutor() {
//    return this.jobExecutor;
//  }
//
//  public ProcessEngineConfigurationImpl setJobExecutor(JobExecutor jobExecutor) {
//    this.jobExecutor = jobExecutor;
//    return this;
//  }
//
//  public PriorityProvider<JobDeclaration<?, ?>> getJobPriorityProvider() {
//    return this.jobPriorityProvider;
//  }
//
//  public void setJobPriorityProvider(PriorityProvider<JobDeclaration<?, ?>> jobPriorityProvider) {
//    this.jobPriorityProvider = jobPriorityProvider;
//  }
//
//  public PriorityProvider<ExternalTaskActivityBehavior> getExternalTaskPriorityProvider() {
//    return this.externalTaskPriorityProvider;
//  }
//
//  public void setExternalTaskPriorityProvider(PriorityProvider<ExternalTaskActivityBehavior> externalTaskPriorityProvider) {
//    this.externalTaskPriorityProvider = externalTaskPriorityProvider;
//  }
//
//  public IdGenerator getIdGenerator() {
//    return this.idGenerator;
//  }
//
//  public ProcessEngineConfigurationImpl setIdGenerator(IdGenerator idGenerator) {
//    this.idGenerator = idGenerator;
//    return this;
//  }
//
//  public String getWsSyncFactoryClassName() {
//    return this.wsSyncFactoryClassName;
//  }
//
//  public ProcessEngineConfigurationImpl setWsSyncFactoryClassName(String wsSyncFactoryClassName) {
//    this.wsSyncFactoryClassName = wsSyncFactoryClassName;
//    return this;
//  }
//
//  public Map<String, FormEngine> getFormEngines() {
//    return this.formEngines;
//  }
//
//  public ProcessEngineConfigurationImpl setFormEngines(Map<String, FormEngine> formEngines) {
//    this.formEngines = formEngines;
//    return this;
//  }
//
//  public FormTypes getFormTypes() {
//    return this.formTypes;
//  }
//
//  public ProcessEngineConfigurationImpl setFormTypes(FormTypes formTypes) {
//    this.formTypes = formTypes;
//    return this;
//  }
//
//  public ScriptingEngines getScriptingEngines() {
//    return this.scriptingEngines;
//  }
//
//  public ProcessEngineConfigurationImpl setScriptingEngines(ScriptingEngines scriptingEngines) {
//    this.scriptingEngines = scriptingEngines;
//    return this;
//  }
//
//  public VariableSerializers getVariableSerializers() {
//    return this.variableSerializers;
//  }
//
//  public VariableSerializerFactory getFallbackSerializerFactory() {
//    return this.fallbackSerializerFactory;
//  }
//
//  public void setFallbackSerializerFactory(VariableSerializerFactory fallbackSerializerFactory) {
//    this.fallbackSerializerFactory = fallbackSerializerFactory;
//  }
//
//  public ProcessEngineConfigurationImpl setVariableTypes(VariableSerializers variableSerializers) {
//    this.variableSerializers = variableSerializers;
//    return this;
//  }
//
//  public ExpressionManager getExpressionManager() {
//    return this.expressionManager;
//  }
//
//  public ProcessEngineConfigurationImpl setExpressionManager(ExpressionManager expressionManager) {
//    this.expressionManager = expressionManager;
//    return this;
//  }
//
//  public BusinessCalendarManager getBusinessCalendarManager() {
//    return this.businessCalendarManager;
//  }
//
//  public ProcessEngineConfigurationImpl setBusinessCalendarManager(BusinessCalendarManager businessCalendarManager) {
//    this.businessCalendarManager = businessCalendarManager;
//    return this;
//  }
//
//  public CommandContextFactory getCommandContextFactory() {
//    return this.commandContextFactory;
//  }
//
//  public ProcessEngineConfigurationImpl setCommandContextFactory(CommandContextFactory commandContextFactory) {
//    this.commandContextFactory = commandContextFactory;
//    return this;
//  }
//
//  public TransactionContextFactory getTransactionContextFactory() {
//    return this.transactionContextFactory;
//  }
//
//  public ProcessEngineConfigurationImpl setTransactionContextFactory(TransactionContextFactory transactionContextFactory) {
//    this.transactionContextFactory = transactionContextFactory;
//    return this;
//  }
//
//  public List<Deployer> getCustomPreDeployers()
//  {
//    return this.customPreDeployers;
//  }
//
//  public ProcessEngineConfigurationImpl setCustomPreDeployers(List<Deployer> customPreDeployers)
//  {
//    this.customPreDeployers = customPreDeployers;
//    return this;
//  }
//
//  public List<Deployer> getCustomPostDeployers()
//  {
//    return this.customPostDeployers;
//  }
//
//  public ProcessEngineConfigurationImpl setCustomPostDeployers(List<Deployer> customPostDeployers)
//  {
//    this.customPostDeployers = customPostDeployers;
//    return this;
//  }
//
//  public void setCacheFactory(CacheFactory cacheFactory) {
//    this.cacheFactory = cacheFactory;
//  }
//
//  public void setCacheCapacity(int cacheCapacity) {
//    this.cacheCapacity = cacheCapacity;
//  }
//
//  public void setEnableFetchProcessDefinitionDescription(boolean enableFetchProcessDefinitionDescription) {
//    this.enableFetchProcessDefinitionDescription = enableFetchProcessDefinitionDescription;
//  }
//
//  public boolean getEnableFetchProcessDefinitionDescription() {
//    return this.enableFetchProcessDefinitionDescription;
//  }
//
//  public Permission getDefaultUserPermissionForTask() {
//    return this.defaultUserPermissionForTask;
//  }
//
//  public ProcessEngineConfigurationImpl setDefaultUserPermissionForTask(Permission defaultUserPermissionForTask) {
//    this.defaultUserPermissionForTask = defaultUserPermissionForTask;
//    return this;
//  }
//
//  public Map<String, JobHandler> getJobHandlers() {
//    return this.jobHandlers;
//  }
//
//  public ProcessEngineConfigurationImpl setJobHandlers(Map<String, JobHandler> jobHandlers)
//  {
//    this.jobHandlers = jobHandlers;
//    return this;
//  }
//
//  public SqlSessionFactory getSqlSessionFactory()
//  {
//    return this.sqlSessionFactory;
//  }
//
//  public ProcessEngineConfigurationImpl setSqlSessionFactory(SqlSessionFactory sqlSessionFactory)
//  {
//    this.sqlSessionFactory = sqlSessionFactory;
//    return this;
//  }
//
//  public DbSqlSessionFactory getDbSqlSessionFactory()
//  {
//    return this.dbSqlSessionFactory;
//  }
//
//  public ProcessEngineConfigurationImpl setDbSqlSessionFactory(DbSqlSessionFactory dbSqlSessionFactory) {
//    this.dbSqlSessionFactory = dbSqlSessionFactory;
//    return this;
//  }
//
//  public TransactionFactory getTransactionFactory() {
//    return this.transactionFactory;
//  }
//
//  public ProcessEngineConfigurationImpl setTransactionFactory(TransactionFactory transactionFactory) {
//    this.transactionFactory = transactionFactory;
//    return this;
//  }
//
//  public List<SessionFactory> getCustomSessionFactories() {
//    return this.customSessionFactories;
//  }
//
//  public ProcessEngineConfigurationImpl setCustomSessionFactories(List<SessionFactory> customSessionFactories) {
//    this.customSessionFactories = customSessionFactories;
//    return this;
//  }
//
//  public List<JobHandler> getCustomJobHandlers() {
//    return this.customJobHandlers;
//  }
//
//  public ProcessEngineConfigurationImpl setCustomJobHandlers(List<JobHandler> customJobHandlers) {
//    this.customJobHandlers = customJobHandlers;
//    return this;
//  }
//
//  public List<FormEngine> getCustomFormEngines() {
//    return this.customFormEngines;
//  }
//
//  public ProcessEngineConfigurationImpl setCustomFormEngines(List<FormEngine> customFormEngines) {
//    this.customFormEngines = customFormEngines;
//    return this;
//  }
//
//  public List<AbstractFormFieldType> getCustomFormTypes() {
//    return this.customFormTypes;
//  }
//
//  public ProcessEngineConfigurationImpl setCustomFormTypes(List<AbstractFormFieldType> customFormTypes)
//  {
//    this.customFormTypes = customFormTypes;
//    return this;
//  }
//
//  public List<TypedValueSerializer> getCustomPreVariableSerializers() {
//    return this.customPreVariableSerializers;
//  }
//
//  public ProcessEngineConfigurationImpl setCustomPreVariableSerializers(List<TypedValueSerializer> customPreVariableTypes)
//  {
//    this.customPreVariableSerializers = customPreVariableTypes;
//    return this;
//  }
//
//  public List<TypedValueSerializer> getCustomPostVariableSerializers()
//  {
//    return this.customPostVariableSerializers;
//  }
//
//  public ProcessEngineConfigurationImpl setCustomPostVariableSerializers(List<TypedValueSerializer> customPostVariableTypes)
//  {
//    this.customPostVariableSerializers = customPostVariableTypes;
//    return this;
//  }
//
//  public List<BpmnParseListener> getCustomPreBPMNParseListeners() {
//    return this.preParseListeners;
//  }
//
//  public void setCustomPreBPMNParseListeners(List<BpmnParseListener> preParseListeners) {
//    this.preParseListeners = preParseListeners;
//  }
//
//  public List<BpmnParseListener> getCustomPostBPMNParseListeners() {
//    return this.postParseListeners;
//  }
//
//  public void setCustomPostBPMNParseListeners(List<BpmnParseListener> postParseListeners) {
//    this.postParseListeners = postParseListeners;
//  }
//
//  @Deprecated
//  public List<BpmnParseListener> getPreParseListeners()
//  {
//    return this.preParseListeners;
//  }
//
//  @Deprecated
//  public void setPreParseListeners(List<BpmnParseListener> preParseListeners)
//  {
//    this.preParseListeners = preParseListeners;
//  }
//
//  @Deprecated
//  public List<BpmnParseListener> getPostParseListeners()
//  {
//    return this.postParseListeners;
//  }
//
//  @Deprecated
//  public void setPostParseListeners(List<BpmnParseListener> postParseListeners)
//  {
//    this.postParseListeners = postParseListeners;
//  }
//
//  public List<CmmnTransformListener> getCustomPreCmmnTransformListeners() {
//    return this.customPreCmmnTransformListeners;
//  }
//
//  public void setCustomPreCmmnTransformListeners(List<CmmnTransformListener> customPreCmmnTransformListeners) {
//    this.customPreCmmnTransformListeners = customPreCmmnTransformListeners;
//  }
//
//  public List<CmmnTransformListener> getCustomPostCmmnTransformListeners() {
//    return this.customPostCmmnTransformListeners;
//  }
//
//  public void setCustomPostCmmnTransformListeners(List<CmmnTransformListener> customPostCmmnTransformListeners) {
//    this.customPostCmmnTransformListeners = customPostCmmnTransformListeners;
//  }
//
//  public Map<Object, Object> getBeans() {
//    return this.beans;
//  }
//
//  public void setBeans(Map<Object, Object> beans) {
//    this.beans = beans;
//  }
//
//  public ProcessEngineConfigurationImpl setClassLoader(ClassLoader classLoader)
//  {
//    super.setClassLoader(classLoader);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setDatabaseType(String databaseType)
//  {
//    super.setDatabaseType(databaseType);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setDataSource(DataSource dataSource)
//  {
//    super.setDataSource(dataSource);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setDatabaseSchemaUpdate(String databaseSchemaUpdate)
//  {
//    super.setDatabaseSchemaUpdate(databaseSchemaUpdate);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setHistory(String history)
//  {
//    super.setHistory(history);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setIdBlockSize(int idBlockSize)
//  {
//    super.setIdBlockSize(idBlockSize);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setJdbcDriver(String jdbcDriver)
//  {
//    super.setJdbcDriver(jdbcDriver);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setJdbcPassword(String jdbcPassword)
//  {
//    super.setJdbcPassword(jdbcPassword);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setJdbcUrl(String jdbcUrl)
//  {
//    super.setJdbcUrl(jdbcUrl);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setJdbcUsername(String jdbcUsername)
//  {
//    super.setJdbcUsername(jdbcUsername);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setJobExecutorActivate(boolean jobExecutorActivate)
//  {
//    super.setJobExecutorActivate(jobExecutorActivate);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setMailServerDefaultFrom(String mailServerDefaultFrom)
//  {
//    super.setMailServerDefaultFrom(mailServerDefaultFrom);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setMailServerHost(String mailServerHost)
//  {
//    super.setMailServerHost(mailServerHost);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setMailServerPassword(String mailServerPassword)
//  {
//    super.setMailServerPassword(mailServerPassword);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setMailServerPort(int mailServerPort)
//  {
//    super.setMailServerPort(mailServerPort);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setMailServerUseTLS(boolean useTLS)
//  {
//    super.setMailServerUseTLS(useTLS);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setMailServerUsername(String mailServerUsername)
//  {
//    super.setMailServerUsername(mailServerUsername);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setJdbcMaxActiveConnections(int jdbcMaxActiveConnections)
//  {
//    super.setJdbcMaxActiveConnections(jdbcMaxActiveConnections);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setJdbcMaxCheckoutTime(int jdbcMaxCheckoutTime)
//  {
//    super.setJdbcMaxCheckoutTime(jdbcMaxCheckoutTime);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setJdbcMaxIdleConnections(int jdbcMaxIdleConnections)
//  {
//    super.setJdbcMaxIdleConnections(jdbcMaxIdleConnections);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setJdbcMaxWaitTime(int jdbcMaxWaitTime)
//  {
//    super.setJdbcMaxWaitTime(jdbcMaxWaitTime);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setTransactionsExternallyManaged(boolean transactionsExternallyManaged)
//  {
//    super.setTransactionsExternallyManaged(transactionsExternallyManaged);
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setJpaEntityManagerFactory(Object jpaEntityManagerFactory)
//  {
//    this.jpaEntityManagerFactory = jpaEntityManagerFactory;
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setJpaHandleTransaction(boolean jpaHandleTransaction)
//  {
//    this.jpaHandleTransaction = jpaHandleTransaction;
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setJpaCloseEntityManager(boolean jpaCloseEntityManager)
//  {
//    this.jpaCloseEntityManager = jpaCloseEntityManager;
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setJdbcPingEnabled(boolean jdbcPingEnabled)
//  {
//    this.jdbcPingEnabled = jdbcPingEnabled;
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setJdbcPingQuery(String jdbcPingQuery)
//  {
//    this.jdbcPingQuery = jdbcPingQuery;
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setJdbcPingConnectionNotUsedFor(int jdbcPingNotUsedFor)
//  {
//    this.jdbcPingConnectionNotUsedFor = jdbcPingNotUsedFor;
//    return this;
//  }
//
//  public boolean isDbIdentityUsed() {
//    return this.isDbIdentityUsed;
//  }
//
//  public void setDbIdentityUsed(boolean isDbIdentityUsed)
//  {
//    this.isDbIdentityUsed = isDbIdentityUsed;
//  }
//
//  public boolean isDbHistoryUsed() {
//    return this.isDbHistoryUsed;
//  }
//
//  public void setDbHistoryUsed(boolean isDbHistoryUsed) {
//    this.isDbHistoryUsed = isDbHistoryUsed;
//  }
//
//  public List<ResolverFactory> getResolverFactories() {
//    return this.resolverFactories;
//  }
//
//  public void setResolverFactories(List<ResolverFactory> resolverFactories) {
//    this.resolverFactories = resolverFactories;
//  }
//
//  public DeploymentCache getDeploymentCache() {
//    return this.deploymentCache;
//  }
//
//  public void setDeploymentCache(DeploymentCache deploymentCache) {
//    this.deploymentCache = deploymentCache;
//  }
//
//  public ProcessEngineConfigurationImpl setDelegateInterceptor(DelegateInterceptor delegateInterceptor) {
//    this.delegateInterceptor = delegateInterceptor;
//    return this;
//  }
//
//  public DelegateInterceptor getDelegateInterceptor() {
//    return this.delegateInterceptor;
//  }
//
//  public RejectedJobsHandler getCustomRejectedJobsHandler() {
//    return this.customRejectedJobsHandler;
//  }
//
//  public ProcessEngineConfigurationImpl setCustomRejectedJobsHandler(RejectedJobsHandler customRejectedJobsHandler) {
//    this.customRejectedJobsHandler = customRejectedJobsHandler;
//    return this;
//  }
//
//  public EventHandler getEventHandler(String eventType) {
//    return (EventHandler)this.eventHandlers.get(eventType);
//  }
//
//  public void setEventHandlers(Map<String, EventHandler> eventHandlers) {
//    this.eventHandlers = eventHandlers;
//  }
//
//  public Map<String, EventHandler> getEventHandlers() {
//    return this.eventHandlers;
//  }
//
//  public List<EventHandler> getCustomEventHandlers() {
//    return this.customEventHandlers;
//  }
//
//  public void setCustomEventHandlers(List<EventHandler> customEventHandlers) {
//    this.customEventHandlers = customEventHandlers;
//  }
//
//  public FailedJobCommandFactory getFailedJobCommandFactory() {
//    return this.failedJobCommandFactory;
//  }
//
//  public ProcessEngineConfigurationImpl setFailedJobCommandFactory(FailedJobCommandFactory failedJobCommandFactory) {
//    this.failedJobCommandFactory = failedJobCommandFactory;
//    return this;
//  }
//
//  public ProcessEngineConfiguration setDatabaseTablePrefix(String databaseTablePrefix)
//  {
//    this.databaseTablePrefix = databaseTablePrefix;
//    return this;
//  }
//
//  public String getDatabaseTablePrefix() {
//    return this.databaseTablePrefix;
//  }
//
//  public boolean isCreateDiagramOnDeploy() {
//    return this.isCreateDiagramOnDeploy;
//  }
//
//  public ProcessEngineConfiguration setCreateDiagramOnDeploy(boolean createDiagramOnDeploy) {
//    this.isCreateDiagramOnDeploy = createDiagramOnDeploy;
//    return this;
//  }
//
//  public String getDatabaseSchema() {
//    return this.databaseSchema;
//  }
//
//  public void setDatabaseSchema(String databaseSchema) {
//    this.databaseSchema = databaseSchema;
//  }
//
//  public DataSource getIdGeneratorDataSource() {
//    return this.idGeneratorDataSource;
//  }
//
//  public void setIdGeneratorDataSource(DataSource idGeneratorDataSource) {
//    this.idGeneratorDataSource = idGeneratorDataSource;
//  }
//
//  public String getIdGeneratorDataSourceJndiName() {
//    return this.idGeneratorDataSourceJndiName;
//  }
//
//  public void setIdGeneratorDataSourceJndiName(String idGeneratorDataSourceJndiName) {
//    this.idGeneratorDataSourceJndiName = idGeneratorDataSourceJndiName;
//  }
//
//  public ProcessApplicationManager getProcessApplicationManager() {
//    return this.processApplicationManager;
//  }
//
//  public void setProcessApplicationManager(ProcessApplicationManager processApplicationManager) {
//    this.processApplicationManager = processApplicationManager;
//  }
//
//  public CommandExecutor getCommandExecutorSchemaOperations() {
//    return this.commandExecutorSchemaOperations;
//  }
//
//  public void setCommandExecutorSchemaOperations(CommandExecutor commandExecutorSchemaOperations) {
//    this.commandExecutorSchemaOperations = commandExecutorSchemaOperations;
//  }
//
//  public CorrelationHandler getCorrelationHandler() {
//    return this.correlationHandler;
//  }
//
//  public void setCorrelationHandler(CorrelationHandler correlationHandler) {
//    this.correlationHandler = correlationHandler;
//  }
//
//  public ConditionHandler getConditionHandler() {
//    return this.conditionHandler;
//  }
//
//  public void setConditionHandler(ConditionHandler conditionHandler) {
//    this.conditionHandler = conditionHandler;
//  }
//
//  public ProcessEngineConfigurationImpl setHistoryEventHandler(HistoryEventHandler historyEventHandler) {
//    this.historyEventHandler = historyEventHandler;
//    return this;
//  }
//
//  public HistoryEventHandler getHistoryEventHandler() {
//    return this.historyEventHandler;
//  }
//
//  public IncidentHandler getIncidentHandler(String incidentType) {
//    return (IncidentHandler)this.incidentHandlers.get(incidentType);
//  }
//
//  public Map<String, IncidentHandler> getIncidentHandlers() {
//    return this.incidentHandlers;
//  }
//
//  public void setIncidentHandlers(Map<String, IncidentHandler> incidentHandlers) {
//    this.incidentHandlers = incidentHandlers;
//  }
//
//  public List<IncidentHandler> getCustomIncidentHandlers() {
//    return this.customIncidentHandlers;
//  }
//
//  public void setCustomIncidentHandlers(List<IncidentHandler> customIncidentHandlers) {
//    this.customIncidentHandlers = customIncidentHandlers;
//  }
//
//  public Map<String, BatchJobHandler<?>> getBatchHandlers() {
//    return this.batchHandlers;
//  }
//
//  public void setBatchHandlers(Map<String, BatchJobHandler<?>> batchHandlers) {
//    this.batchHandlers = batchHandlers;
//  }
//
//  public List<BatchJobHandler<?>> getCustomBatchJobHandlers() {
//    return this.customBatchJobHandlers;
//  }
//
//  public void setCustomBatchJobHandlers(List<BatchJobHandler<?>> customBatchJobHandlers) {
//    this.customBatchJobHandlers = customBatchJobHandlers;
//  }
//
//  public int getBatchJobsPerSeed() {
//    return this.batchJobsPerSeed;
//  }
//
//  public void setBatchJobsPerSeed(int batchJobsPerSeed) {
//    this.batchJobsPerSeed = batchJobsPerSeed;
//  }
//
//  public int getInvocationsPerBatchJob() {
//    return this.invocationsPerBatchJob;
//  }
//
//  public void setInvocationsPerBatchJob(int invocationsPerBatchJob) {
//    this.invocationsPerBatchJob = invocationsPerBatchJob;
//  }
//
//  public int getBatchPollTime() {
//    return this.batchPollTime;
//  }
//
//  public void setBatchPollTime(int batchPollTime) {
//    this.batchPollTime = batchPollTime;
//  }
//
//  public long getBatchJobPriority() {
//    return this.batchJobPriority;
//  }
//
//  public void setBatchJobPriority(long batchJobPriority) {
//    this.batchJobPriority = batchJobPriority;
//  }
//
//  public SessionFactory getIdentityProviderSessionFactory() {
//    return this.identityProviderSessionFactory;
//  }
//
//  public void setIdentityProviderSessionFactory(SessionFactory identityProviderSessionFactory) {
//    this.identityProviderSessionFactory = identityProviderSessionFactory;
//  }
//
//  public SaltGenerator getSaltGenerator() {
//    return this.saltGenerator;
//  }
//
//  public void setSaltGenerator(SaltGenerator saltGenerator) {
//    this.saltGenerator = saltGenerator;
//  }
//
//  public void setPasswordEncryptor(PasswordEncryptor passwordEncryptor) {
//    this.passwordEncryptor = passwordEncryptor;
//  }
//
//  public PasswordEncryptor getPasswordEncryptor() {
//    return this.passwordEncryptor;
//  }
//
//  public List<PasswordEncryptor> getCustomPasswordChecker() {
//    return this.customPasswordChecker;
//  }
//
//  public void setCustomPasswordChecker(List<PasswordEncryptor> customPasswordChecker) {
//    this.customPasswordChecker = customPasswordChecker;
//  }
//
//  public PasswordManager getPasswordManager() {
//    return this.passwordManager;
//  }
//
//  public void setPasswordManager(PasswordManager passwordManager) {
//    this.passwordManager = passwordManager;
//  }
//
//  public Set<String> getRegisteredDeployments() {
//    return this.registeredDeployments;
//  }
//
//  public void setRegisteredDeployments(Set<String> registeredDeployments) {
//    this.registeredDeployments = registeredDeployments;
//  }
//
//  public ResourceAuthorizationProvider getResourceAuthorizationProvider() {
//    return this.resourceAuthorizationProvider;
//  }
//
//  public void setResourceAuthorizationProvider(ResourceAuthorizationProvider resourceAuthorizationProvider) {
//    this.resourceAuthorizationProvider = resourceAuthorizationProvider;
//  }
//
//  public List<ProcessEnginePlugin> getProcessEnginePlugins() {
//    return this.processEnginePlugins;
//  }
//
//  public void setProcessEnginePlugins(List<ProcessEnginePlugin> processEnginePlugins) {
//    this.processEnginePlugins = processEnginePlugins;
//  }
//
//  public ProcessEngineConfigurationImpl setHistoryEventProducer(HistoryEventProducer historyEventProducer) {
//    this.historyEventProducer = historyEventProducer;
//    return this;
//  }
//
//  public HistoryEventProducer getHistoryEventProducer() {
//    return this.historyEventProducer;
//  }
//
//  public ProcessEngineConfigurationImpl setCmmnHistoryEventProducer(CmmnHistoryEventProducer cmmnHistoryEventProducer) {
//    this.cmmnHistoryEventProducer = cmmnHistoryEventProducer;
//    return this;
//  }
//
//  public CmmnHistoryEventProducer getCmmnHistoryEventProducer() {
//    return this.cmmnHistoryEventProducer;
//  }
//
//  public ProcessEngineConfigurationImpl setDmnHistoryEventProducer(DmnHistoryEventProducer dmnHistoryEventProducer) {
//    this.dmnHistoryEventProducer = dmnHistoryEventProducer;
//    return this;
//  }
//
//  public DmnHistoryEventProducer getDmnHistoryEventProducer() {
//    return this.dmnHistoryEventProducer;
//  }
//
//  public Map<String, Class<? extends FormFieldValidator>> getCustomFormFieldValidators() {
//    return this.customFormFieldValidators;
//  }
//
//  public void setCustomFormFieldValidators(Map<String, Class<? extends FormFieldValidator>> customFormFieldValidators) {
//    this.customFormFieldValidators = customFormFieldValidators;
//  }
//
//  public void setFormValidators(FormValidators formValidators) {
//    this.formValidators = formValidators;
//  }
//
//  public FormValidators getFormValidators() {
//    return this.formValidators;
//  }
//
//  public boolean isExecutionTreePrefetchEnabled() {
//    return this.isExecutionTreePrefetchEnabled;
//  }
//
//  public void setExecutionTreePrefetchEnabled(boolean isExecutionTreePrefetchingEnabled) {
//    this.isExecutionTreePrefetchEnabled = isExecutionTreePrefetchingEnabled;
//  }
//
//  public ProcessEngineImpl getProcessEngine() {
//    return this.processEngine;
//  }
//
//  public void setAutoStoreScriptVariables(boolean autoStoreScriptVariables)
//  {
//    this.autoStoreScriptVariables = autoStoreScriptVariables;
//  }
//
//  public boolean isAutoStoreScriptVariables()
//  {
//    return this.autoStoreScriptVariables;
//  }
//
//  public void setEnableScriptCompilation(boolean enableScriptCompilation)
//  {
//    this.enableScriptCompilation = enableScriptCompilation;
//  }
//
//  public boolean isEnableScriptCompilation()
//  {
//    return this.enableScriptCompilation;
//  }
//
//  public boolean isEnableGracefulDegradationOnContextSwitchFailure() {
//    return this.enableGracefulDegradationOnContextSwitchFailure;
//  }
//
//  public void setEnableGracefulDegradationOnContextSwitchFailure(boolean enableGracefulDegradationOnContextSwitchFailure)
//  {
//    this.enableGracefulDegradationOnContextSwitchFailure = enableGracefulDegradationOnContextSwitchFailure;
//  }
//
//  public boolean isDeploymentLockUsed()
//  {
//    return this.isDeploymentLockUsed;
//  }
//
//  public void setDeploymentLockUsed(boolean isDeploymentLockUsed)
//  {
//    this.isDeploymentLockUsed = isDeploymentLockUsed;
//  }
//
//  public boolean isDeploymentSynchronized()
//  {
//    return this.isDeploymentSynchronized;
//  }
//
//  public void setDeploymentSynchronized(boolean deploymentSynchronized)
//  {
//    this.isDeploymentSynchronized = deploymentSynchronized;
//  }
//
//  public boolean isCmmnEnabled() {
//    return this.cmmnEnabled;
//  }
//
//  public void setCmmnEnabled(boolean cmmnEnabled) {
//    this.cmmnEnabled = cmmnEnabled;
//  }
//
//  public boolean isDmnEnabled() {
//    return this.dmnEnabled;
//  }
//
//  public void setDmnEnabled(boolean dmnEnabled) {
//    this.dmnEnabled = dmnEnabled;
//  }
//
//  public ScriptFactory getScriptFactory() {
//    return this.scriptFactory;
//  }
//
//  public ScriptingEnvironment getScriptingEnvironment() {
//    return this.scriptingEnvironment;
//  }
//
//  public void setScriptFactory(ScriptFactory scriptFactory) {
//    this.scriptFactory = scriptFactory;
//  }
//
//  public void setScriptingEnvironment(ScriptingEnvironment scriptingEnvironment) {
//    this.scriptingEnvironment = scriptingEnvironment;
//  }
//
//  public List<ScriptEnvResolver> getEnvScriptResolvers() {
//    return this.scriptEnvResolvers;
//  }
//
//  public void setEnvScriptResolvers(List<ScriptEnvResolver> scriptEnvResolvers) {
//    this.scriptEnvResolvers = scriptEnvResolvers;
//  }
//
//  public ProcessEngineConfiguration setArtifactFactory(ArtifactFactory artifactFactory) {
//    this.artifactFactory = artifactFactory;
//    return this;
//  }
//
//  public ArtifactFactory getArtifactFactory() {
//    return this.artifactFactory;
//  }
//
//  public String getDefaultSerializationFormat() {
//    return this.defaultSerializationFormat;
//  }
//
//  public ProcessEngineConfigurationImpl setDefaultSerializationFormat(String defaultSerializationFormat) {
//    this.defaultSerializationFormat = defaultSerializationFormat;
//    return this;
//  }
//
//  public boolean isJavaSerializationFormatEnabled() {
//    return this.javaSerializationFormatEnabled;
//  }
//
//  public void setJavaSerializationFormatEnabled(boolean javaSerializationFormatEnabled) {
//    this.javaSerializationFormatEnabled = javaSerializationFormatEnabled;
//  }
//
//  public ProcessEngineConfigurationImpl setDefaultCharsetName(String defaultCharsetName) {
//    this.defaultCharsetName = defaultCharsetName;
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setDefaultCharset(Charset defautlCharset) {
//    this.defaultCharset = defautlCharset;
//    return this;
//  }
//
//  public Charset getDefaultCharset() {
//    return this.defaultCharset;
//  }
//
//  public boolean isDbEntityCacheReuseEnabled() {
//    return this.isDbEntityCacheReuseEnabled;
//  }
//
//  public ProcessEngineConfigurationImpl setDbEntityCacheReuseEnabled(boolean isDbEntityCacheReuseEnabled) {
//    this.isDbEntityCacheReuseEnabled = isDbEntityCacheReuseEnabled;
//    return this;
//  }
//
//  public DbEntityCacheKeyMapping getDbEntityCacheKeyMapping() {
//    return this.dbEntityCacheKeyMapping;
//  }
//
//  public ProcessEngineConfigurationImpl setDbEntityCacheKeyMapping(DbEntityCacheKeyMapping dbEntityCacheKeyMapping) {
//    this.dbEntityCacheKeyMapping = dbEntityCacheKeyMapping;
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setCustomHistoryLevels(List<HistoryLevel> customHistoryLevels) {
//    this.customHistoryLevels = customHistoryLevels;
//    return this;
//  }
//
//  public List<HistoryLevel> getHistoryLevels() {
//    return this.historyLevels;
//  }
//
//  public List<HistoryLevel> getCustomHistoryLevels() {
//    return this.customHistoryLevels;
//  }
//
//  public boolean isInvokeCustomVariableListeners() {
//    return this.isInvokeCustomVariableListeners;
//  }
//
//  public ProcessEngineConfigurationImpl setInvokeCustomVariableListeners(boolean isInvokeCustomVariableListeners) {
//    this.isInvokeCustomVariableListeners = isInvokeCustomVariableListeners;
//    return this;
//  }
//
//  public void close() {
//    if ((this.forceCloseMybatisConnectionPool) && ((this.dataSource instanceof PooledDataSource)))
//    {
//      ((PooledDataSource)this.dataSource).forceCloseAll();
//    }
//  }
//
//  public MetricsRegistry getMetricsRegistry() {
//    return this.metricsRegistry;
//  }
//
//  public ProcessEngineConfigurationImpl setMetricsRegistry(MetricsRegistry metricsRegistry) {
//    this.metricsRegistry = metricsRegistry;
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setMetricsEnabled(boolean isMetricsEnabled) {
//    this.isMetricsEnabled = isMetricsEnabled;
//    return this;
//  }
//
//  public boolean isMetricsEnabled() {
//    return this.isMetricsEnabled;
//  }
//
//  public DbMetricsReporter getDbMetricsReporter() {
//    return this.dbMetricsReporter;
//  }
//
//  public ProcessEngineConfigurationImpl setDbMetricsReporter(DbMetricsReporter dbMetricsReporter) {
//    this.dbMetricsReporter = dbMetricsReporter;
//    return this;
//  }
//
//  public boolean isDbMetricsReporterActivate() {
//    return this.isDbMetricsReporterActivate;
//  }
//
//  public ProcessEngineConfigurationImpl setDbMetricsReporterActivate(boolean isDbMetricsReporterEnabled) {
//    this.isDbMetricsReporterActivate = isDbMetricsReporterEnabled;
//    return this;
//  }
//
//  public MetricsReporterIdProvider getMetricsReporterIdProvider() {
//    return this.metricsReporterIdProvider;
//  }
//
//  public void setMetricsReporterIdProvider(MetricsReporterIdProvider metricsReporterIdProvider) {
//    this.metricsReporterIdProvider = metricsReporterIdProvider;
//  }
//
//  public boolean isEnableScriptEngineCaching() {
//    return this.enableScriptEngineCaching;
//  }
//
//  public ProcessEngineConfigurationImpl setEnableScriptEngineCaching(boolean enableScriptEngineCaching) {
//    this.enableScriptEngineCaching = enableScriptEngineCaching;
//    return this;
//  }
//
//  public boolean isEnableFetchScriptEngineFromProcessApplication() {
//    return this.enableFetchScriptEngineFromProcessApplication;
//  }
//
//  public ProcessEngineConfigurationImpl setEnableFetchScriptEngineFromProcessApplication(boolean enable) {
//    this.enableFetchScriptEngineFromProcessApplication = enable;
//    return this;
//  }
//
//  public boolean isEnableExpressionsInAdhocQueries() {
//    return this.enableExpressionsInAdhocQueries;
//  }
//
//  public void setEnableExpressionsInAdhocQueries(boolean enableExpressionsInAdhocQueries) {
//    this.enableExpressionsInAdhocQueries = enableExpressionsInAdhocQueries;
//  }
//
//  public boolean isEnableExpressionsInStoredQueries() {
//    return this.enableExpressionsInStoredQueries;
//  }
//
//  public void setEnableExpressionsInStoredQueries(boolean enableExpressionsInStoredQueries) {
//    this.enableExpressionsInStoredQueries = enableExpressionsInStoredQueries;
//  }
//
//  public boolean isEnableXxeProcessing() {
//    return this.enableXxeProcessing;
//  }
//
//  public void setEnableXxeProcessing(boolean enableXxeProcessing) {
//    this.enableXxeProcessing = enableXxeProcessing;
//  }
//
//  public ProcessEngineConfigurationImpl setBpmnStacktraceVerbose(boolean isBpmnStacktraceVerbose) {
//    this.isBpmnStacktraceVerbose = isBpmnStacktraceVerbose;
//    return this;
//  }
//
//  public boolean isBpmnStacktraceVerbose() {
//    return this.isBpmnStacktraceVerbose;
//  }
//
//  public boolean isForceCloseMybatisConnectionPool() {
//    return this.forceCloseMybatisConnectionPool;
//  }
//
//  public ProcessEngineConfigurationImpl setForceCloseMybatisConnectionPool(boolean forceCloseMybatisConnectionPool) {
//    this.forceCloseMybatisConnectionPool = forceCloseMybatisConnectionPool;
//    return this;
//  }
//
//  public boolean isRestrictUserOperationLogToAuthenticatedUsers() {
//    return this.restrictUserOperationLogToAuthenticatedUsers;
//  }
//
//  public ProcessEngineConfigurationImpl setRestrictUserOperationLogToAuthenticatedUsers(boolean restrictUserOperationLogToAuthenticatedUsers) {
//    this.restrictUserOperationLogToAuthenticatedUsers = restrictUserOperationLogToAuthenticatedUsers;
//    return this;
//  }
//
//  public ProcessEngineConfigurationImpl setTenantIdProvider(TenantIdProvider tenantIdProvider) {
//    this.tenantIdProvider = tenantIdProvider;
//    return this;
//  }
//
//  public TenantIdProvider getTenantIdProvider() {
//    return this.tenantIdProvider;
//  }
//
//  public void setMigrationActivityMatcher(MigrationActivityMatcher migrationActivityMatcher) {
//    this.migrationActivityMatcher = migrationActivityMatcher;
//  }
//
//  public MigrationActivityMatcher getMigrationActivityMatcher() {
//    return this.migrationActivityMatcher;
//  }
//
//  public void setCustomPreMigrationActivityValidators(List<MigrationActivityValidator> customPreMigrationActivityValidators)
//  {
//    this.customPreMigrationActivityValidators = customPreMigrationActivityValidators;
//  }
//
//  public List<MigrationActivityValidator> getCustomPreMigrationActivityValidators() {
//    return this.customPreMigrationActivityValidators;
//  }
//
//  public void setCustomPostMigrationActivityValidators(List<MigrationActivityValidator> customPostMigrationActivityValidators) {
//    this.customPostMigrationActivityValidators = customPostMigrationActivityValidators;
//  }
//
//  public List<MigrationActivityValidator> getCustomPostMigrationActivityValidators() {
//    return this.customPostMigrationActivityValidators;
//  }
//
//  public List<MigrationActivityValidator> getDefaultMigrationActivityValidators() {
//    List migrationActivityValidators = new ArrayList();
//    migrationActivityValidators.add(SupportedActivityValidator.INSTANCE);
//    migrationActivityValidators.add(SupportedPassiveEventTriggerActivityValidator.INSTANCE);
//    migrationActivityValidators.add(NoCompensationHandlerActivityValidator.INSTANCE);
//    return migrationActivityValidators;
//  }
//
//  public void setMigrationInstructionGenerator(MigrationInstructionGenerator migrationInstructionGenerator) {
//    this.migrationInstructionGenerator = migrationInstructionGenerator;
//  }
//
//  public MigrationInstructionGenerator getMigrationInstructionGenerator() {
//    return this.migrationInstructionGenerator;
//  }
//
//  public void setMigrationInstructionValidators(List<MigrationInstructionValidator> migrationInstructionValidators) {
//    this.migrationInstructionValidators = migrationInstructionValidators;
//  }
//
//  public List<MigrationInstructionValidator> getMigrationInstructionValidators() {
//    return this.migrationInstructionValidators;
//  }
//
//  public void setCustomPostMigrationInstructionValidators(List<MigrationInstructionValidator> customPostMigrationInstructionValidators) {
//    this.customPostMigrationInstructionValidators = customPostMigrationInstructionValidators;
//  }
//
//  public List<MigrationInstructionValidator> getCustomPostMigrationInstructionValidators() {
//    return this.customPostMigrationInstructionValidators;
//  }
//
//  public void setCustomPreMigrationInstructionValidators(List<MigrationInstructionValidator> customPreMigrationInstructionValidators) {
//    this.customPreMigrationInstructionValidators = customPreMigrationInstructionValidators;
//  }
//
//  public List<MigrationInstructionValidator> getCustomPreMigrationInstructionValidators() {
//    return this.customPreMigrationInstructionValidators;
//  }
//
//  public List<MigrationInstructionValidator> getDefaultMigrationInstructionValidators()
//  {
//    List migrationInstructionValidators = new ArrayList();
//    migrationInstructionValidators.add(new SameBehaviorInstructionValidator());
//    migrationInstructionValidators.add(new SameEventTypeValidator());
//    migrationInstructionValidators.add(new OnlyOnceMappedActivityInstructionValidator());
//    migrationInstructionValidators.add(new CannotAddMultiInstanceBodyValidator());
//    migrationInstructionValidators.add(new CannotAddMultiInstanceInnerActivityValidator());
//    migrationInstructionValidators.add(new CannotRemoveMultiInstanceInnerActivityValidator());
//    migrationInstructionValidators.add(new GatewayMappingValidator());
//    migrationInstructionValidators.add(new SameEventScopeInstructionValidator());
//    migrationInstructionValidators.add(new UpdateEventTriggersValidator());
//    migrationInstructionValidators.add(new AdditionalFlowScopeInstructionValidator());
//    migrationInstructionValidators.add(new ConditionalEventUpdateEventTriggerValidator());
//    return migrationInstructionValidators;
//  }
//
//  public void setMigratingActivityInstanceValidators(List<MigratingActivityInstanceValidator> migratingActivityInstanceValidators) {
//    this.migratingActivityInstanceValidators = migratingActivityInstanceValidators;
//  }
//
//  public List<MigratingActivityInstanceValidator> getMigratingActivityInstanceValidators() {
//    return this.migratingActivityInstanceValidators;
//  }
//
//  public void setCustomPostMigratingActivityInstanceValidators(List<MigratingActivityInstanceValidator> customPostMigratingActivityInstanceValidators) {
//    this.customPostMigratingActivityInstanceValidators = customPostMigratingActivityInstanceValidators;
//  }
//
//  public List<MigratingActivityInstanceValidator> getCustomPostMigratingActivityInstanceValidators() {
//    return this.customPostMigratingActivityInstanceValidators;
//  }
//
//  public void setCustomPreMigratingActivityInstanceValidators(List<MigratingActivityInstanceValidator> customPreMigratingActivityInstanceValidators) {
//    this.customPreMigratingActivityInstanceValidators = customPreMigratingActivityInstanceValidators;
//  }
//
//  public List<MigratingActivityInstanceValidator> getCustomPreMigratingActivityInstanceValidators() {
//    return this.customPreMigratingActivityInstanceValidators;
//  }
//
//  public List<MigratingTransitionInstanceValidator> getMigratingTransitionInstanceValidators() {
//    return this.migratingTransitionInstanceValidators;
//  }
//
//  public List<MigratingCompensationInstanceValidator> getMigratingCompensationInstanceValidators() {
//    return this.migratingCompensationInstanceValidators;
//  }
//
//  public List<MigratingActivityInstanceValidator> getDefaultMigratingActivityInstanceValidators() {
//    List migratingActivityInstanceValidators = new ArrayList();
//
//    migratingActivityInstanceValidators.add(new NoUnmappedLeafInstanceValidator());
//    migratingActivityInstanceValidators.add(new VariableConflictActivityInstanceValidator());
//    migratingActivityInstanceValidators.add(new SupportedActivityInstanceValidator());
//
//    return migratingActivityInstanceValidators;
//  }
//
//  public List<MigratingTransitionInstanceValidator> getDefaultMigratingTransitionInstanceValidators() {
//    List migratingTransitionInstanceValidators = new ArrayList();
//
//    migratingTransitionInstanceValidators.add(new NoUnmappedLeafInstanceValidator());
//    migratingTransitionInstanceValidators.add(new AsyncAfterMigrationValidator());
//    migratingTransitionInstanceValidators.add(new AsyncProcessStartMigrationValidator());
//    migratingTransitionInstanceValidators.add(new AsyncMigrationValidator());
//
//    return migratingTransitionInstanceValidators;
//  }
//
//  public List<CommandChecker> getCommandCheckers() {
//    return this.commandCheckers;
//  }
//
//  public void setCommandCheckers(List<CommandChecker> commandCheckers) {
//    this.commandCheckers = commandCheckers;
//  }
//
//  public ProcessEngineConfigurationImpl setUseSharedSqlSessionFactory(boolean isUseSharedSqlSessionFactory) {
//    this.isUseSharedSqlSessionFactory = isUseSharedSqlSessionFactory;
//    return this;
//  }
//
//  public boolean isUseSharedSqlSessionFactory() {
//    return this.isUseSharedSqlSessionFactory;
//  }
//
//  public boolean getDisableStrictCallActivityValidation() {
//    return this.disableStrictCallActivityValidation;
//  }
//
//  public void setDisableStrictCallActivityValidation(boolean disableStrictCallActivityValidation) {
//    this.disableStrictCallActivityValidation = disableStrictCallActivityValidation;
//  }
//
//  public String getHistoryCleanupBatchWindowStartTime() {
//    return this.historyCleanupBatchWindowStartTime;
//  }
//
//  public void setHistoryCleanupBatchWindowStartTime(String historyCleanupBatchWindowStartTime) {
//    this.historyCleanupBatchWindowStartTime = historyCleanupBatchWindowStartTime;
//  }
//
//  public String getHistoryCleanupBatchWindowEndTime() {
//    return this.historyCleanupBatchWindowEndTime;
//  }
//
//  public void setHistoryCleanupBatchWindowEndTime(String historyCleanupBatchWindowEndTime) {
//    this.historyCleanupBatchWindowEndTime = historyCleanupBatchWindowEndTime;
//  }
//
//  public String getMondayHistoryCleanupBatchWindowStartTime() {
//    return this.mondayHistoryCleanupBatchWindowStartTime;
//  }
//
//  public void setMondayHistoryCleanupBatchWindowStartTime(String mondayHistoryCleanupBatchWindowStartTime) {
//    this.mondayHistoryCleanupBatchWindowStartTime = mondayHistoryCleanupBatchWindowStartTime;
//  }
//
//  public String getMondayHistoryCleanupBatchWindowEndTime() {
//    return this.mondayHistoryCleanupBatchWindowEndTime;
//  }
//
//  public void setMondayHistoryCleanupBatchWindowEndTime(String mondayHistoryCleanupBatchWindowEndTime) {
//    this.mondayHistoryCleanupBatchWindowEndTime = mondayHistoryCleanupBatchWindowEndTime;
//  }
//
//  public String getTuesdayHistoryCleanupBatchWindowStartTime() {
//    return this.tuesdayHistoryCleanupBatchWindowStartTime;
//  }
//
//  public void setTuesdayHistoryCleanupBatchWindowStartTime(String tuesdayHistoryCleanupBatchWindowStartTime) {
//    this.tuesdayHistoryCleanupBatchWindowStartTime = tuesdayHistoryCleanupBatchWindowStartTime;
//  }
//
//  public String getTuesdayHistoryCleanupBatchWindowEndTime() {
//    return this.tuesdayHistoryCleanupBatchWindowEndTime;
//  }
//
//  public void setTuesdayHistoryCleanupBatchWindowEndTime(String tuesdayHistoryCleanupBatchWindowEndTime) {
//    this.tuesdayHistoryCleanupBatchWindowEndTime = tuesdayHistoryCleanupBatchWindowEndTime;
//  }
//
//  public String getWednesdayHistoryCleanupBatchWindowStartTime() {
//    return this.wednesdayHistoryCleanupBatchWindowStartTime;
//  }
//
//  public void setWednesdayHistoryCleanupBatchWindowStartTime(String wednesdayHistoryCleanupBatchWindowStartTime) {
//    this.wednesdayHistoryCleanupBatchWindowStartTime = wednesdayHistoryCleanupBatchWindowStartTime;
//  }
//
//  public String getWednesdayHistoryCleanupBatchWindowEndTime() {
//    return this.wednesdayHistoryCleanupBatchWindowEndTime;
//  }
//
//  public void setWednesdayHistoryCleanupBatchWindowEndTime(String wednesdayHistoryCleanupBatchWindowEndTime) {
//    this.wednesdayHistoryCleanupBatchWindowEndTime = wednesdayHistoryCleanupBatchWindowEndTime;
//  }
//
//  public String getThursdayHistoryCleanupBatchWindowStartTime() {
//    return this.thursdayHistoryCleanupBatchWindowStartTime;
//  }
//
//  public void setThursdayHistoryCleanupBatchWindowStartTime(String thursdayHistoryCleanupBatchWindowStartTime) {
//    this.thursdayHistoryCleanupBatchWindowStartTime = thursdayHistoryCleanupBatchWindowStartTime;
//  }
//
//  public String getThursdayHistoryCleanupBatchWindowEndTime() {
//    return this.thursdayHistoryCleanupBatchWindowEndTime;
//  }
//
//  public void setThursdayHistoryCleanupBatchWindowEndTime(String thursdayHistoryCleanupBatchWindowEndTime) {
//    this.thursdayHistoryCleanupBatchWindowEndTime = thursdayHistoryCleanupBatchWindowEndTime;
//  }
//
//  public String getFridayHistoryCleanupBatchWindowStartTime() {
//    return this.fridayHistoryCleanupBatchWindowStartTime;
//  }
//
//  public void setFridayHistoryCleanupBatchWindowStartTime(String fridayHistoryCleanupBatchWindowStartTime) {
//    this.fridayHistoryCleanupBatchWindowStartTime = fridayHistoryCleanupBatchWindowStartTime;
//  }
//
//  public String getFridayHistoryCleanupBatchWindowEndTime() {
//    return this.fridayHistoryCleanupBatchWindowEndTime;
//  }
//
//  public void setFridayHistoryCleanupBatchWindowEndTime(String fridayHistoryCleanupBatchWindowEndTime) {
//    this.fridayHistoryCleanupBatchWindowEndTime = fridayHistoryCleanupBatchWindowEndTime;
//  }
//
//  public String getSaturdayHistoryCleanupBatchWindowStartTime() {
//    return this.saturdayHistoryCleanupBatchWindowStartTime;
//  }
//
//  public void setSaturdayHistoryCleanupBatchWindowStartTime(String saturdayHistoryCleanupBatchWindowStartTime) {
//    this.saturdayHistoryCleanupBatchWindowStartTime = saturdayHistoryCleanupBatchWindowStartTime;
//  }
//
//  public String getSaturdayHistoryCleanupBatchWindowEndTime() {
//    return this.saturdayHistoryCleanupBatchWindowEndTime;
//  }
//
//  public void setSaturdayHistoryCleanupBatchWindowEndTime(String saturdayHistoryCleanupBatchWindowEndTime) {
//    this.saturdayHistoryCleanupBatchWindowEndTime = saturdayHistoryCleanupBatchWindowEndTime;
//  }
//
//  public String getSundayHistoryCleanupBatchWindowStartTime() {
//    return this.sundayHistoryCleanupBatchWindowStartTime;
//  }
//
//  public void setSundayHistoryCleanupBatchWindowStartTime(String sundayHistoryCleanupBatchWindowStartTime) {
//    this.sundayHistoryCleanupBatchWindowStartTime = sundayHistoryCleanupBatchWindowStartTime;
//  }
//
//  public String getSundayHistoryCleanupBatchWindowEndTime() {
//    return this.sundayHistoryCleanupBatchWindowEndTime;
//  }
//
//  public void setSundayHistoryCleanupBatchWindowEndTime(String sundayHistoryCleanupBatchWindowEndTime) {
//    this.sundayHistoryCleanupBatchWindowEndTime = sundayHistoryCleanupBatchWindowEndTime;
//  }
//
//  public Date getHistoryCleanupBatchWindowStartTimeAsDate() {
//    return this.historyCleanupBatchWindowStartTimeAsDate;
//  }
//
//  public void setHistoryCleanupBatchWindowStartTimeAsDate(Date historyCleanupBatchWindowStartTimeAsDate) {
//    this.historyCleanupBatchWindowStartTimeAsDate = historyCleanupBatchWindowStartTimeAsDate;
//  }
//
//  public void setHistoryCleanupBatchWindowEndTimeAsDate(Date historyCleanupBatchWindowEndTimeAsDate) {
//    this.historyCleanupBatchWindowEndTimeAsDate = historyCleanupBatchWindowEndTimeAsDate;
//  }
//
//  public Date getHistoryCleanupBatchWindowEndTimeAsDate() {
//    return this.historyCleanupBatchWindowEndTimeAsDate;
//  }
//
//  public Map<Integer, BatchWindowConfiguration> getHistoryCleanupBatchWindows() {
//    return this.historyCleanupBatchWindows;
//  }
//
//  public void setHistoryCleanupBatchWindows(Map<Integer, BatchWindowConfiguration> historyCleanupBatchWindows) {
//    this.historyCleanupBatchWindows = historyCleanupBatchWindows;
//  }
//
//  public int getHistoryCleanupBatchSize() {
//    return this.historyCleanupBatchSize;
//  }
//
//  public void setHistoryCleanupBatchSize(int historyCleanupBatchSize) {
//    this.historyCleanupBatchSize = historyCleanupBatchSize;
//  }
//
//  public int getHistoryCleanupBatchThreshold() {
//    return this.historyCleanupBatchThreshold;
//  }
//
//  public void setHistoryCleanupBatchThreshold(int historyCleanupBatchThreshold) {
//    this.historyCleanupBatchThreshold = historyCleanupBatchThreshold;
//  }
//
//  public boolean isHistoryCleanupMetricsEnabled() {
//    return this.historyCleanupMetricsEnabled;
//  }
//
//  public void setHistoryCleanupMetricsEnabled(boolean historyCleanupMetricsEnabled) {
//    this.historyCleanupMetricsEnabled = historyCleanupMetricsEnabled;
//  }
//
//  public String getBatchOperationHistoryTimeToLive() {
//    return this.batchOperationHistoryTimeToLive;
//  }
//
//  public int getHistoryCleanupDegreeOfParallelism() {
//    return this.historyCleanupDegreeOfParallelism;
//  }
//
//  public void setHistoryCleanupDegreeOfParallelism(int historyCleanupDegreeOfParallelism) {
//    this.historyCleanupDegreeOfParallelism = historyCleanupDegreeOfParallelism;
//  }
//
//  public void setBatchOperationHistoryTimeToLive(String batchOperationHistoryTimeToLive) {
//    this.batchOperationHistoryTimeToLive = batchOperationHistoryTimeToLive;
//  }
//
//  public Map<String, String> getBatchOperationsForHistoryCleanup() {
//    return this.batchOperationsForHistoryCleanup;
//  }
//
//  public void setBatchOperationsForHistoryCleanup(Map<String, String> batchOperationsForHistoryCleanup) {
//    this.batchOperationsForHistoryCleanup = batchOperationsForHistoryCleanup;
//  }
//
//  public Map<String, Integer> getParsedBatchOperationsForHistoryCleanup() {
//    return this.parsedBatchOperationsForHistoryCleanup;
//  }
//
//  public void setParsedBatchOperationsForHistoryCleanup(Map<String, Integer> parsedBatchOperationsForHistoryCleanup) {
//    this.parsedBatchOperationsForHistoryCleanup = parsedBatchOperationsForHistoryCleanup;
//  }
//
//  public BatchWindowManager getBatchWindowManager() {
//    return this.batchWindowManager;
//  }
//
//  public void setBatchWindowManager(BatchWindowManager batchWindowManager) {
//    this.batchWindowManager = batchWindowManager;
//  }
//
//  public HistoryRemovalTimeProvider getHistoryRemovalTimeProvider() {
//    return this.historyRemovalTimeProvider;
//  }
//
//  public ProcessEngineConfigurationImpl setHistoryRemovalTimeProvider(HistoryRemovalTimeProvider removalTimeProvider) {
//    this.historyRemovalTimeProvider = removalTimeProvider;
//    return this;
//  }
//
//  public String getHistoryRemovalTimeStrategy() {
//    return this.historyRemovalTimeStrategy;
//  }
//
//  public ProcessEngineConfigurationImpl setHistoryRemovalTimeStrategy(String removalTimeStrategy) {
//    this.historyRemovalTimeStrategy = removalTimeStrategy;
//    return this;
//  }
//
//  public String getHistoryCleanupStrategy() {
//    return this.historyCleanupStrategy;
//  }
//
//  public ProcessEngineConfigurationImpl setHistoryCleanupStrategy(String historyCleanupStrategy) {
//    this.historyCleanupStrategy = historyCleanupStrategy;
//    return this;
//  }
//
//  public int getFailedJobListenerMaxRetries() {
//    return this.failedJobListenerMaxRetries;
//  }
//
//  public void setFailedJobListenerMaxRetries(int failedJobListenerMaxRetries) {
//    this.failedJobListenerMaxRetries = failedJobListenerMaxRetries;
//  }
//
//  public String getFailedJobRetryTimeCycle() {
//    return this.failedJobRetryTimeCycle;
//  }
//
//  public void setFailedJobRetryTimeCycle(String failedJobRetryTimeCycle) {
//    this.failedJobRetryTimeCycle = failedJobRetryTimeCycle;
//  }
//
//  public int getLoginMaxAttempts() {
//    return this.loginMaxAttempts;
//  }
//
//  public void setLoginMaxAttempts(int loginMaxAttempts) {
//    this.loginMaxAttempts = loginMaxAttempts;
//  }
//
//  public int getLoginDelayFactor() {
//    return this.loginDelayFactor;
//  }
//
//  public void setLoginDelayFactor(int loginDelayFactor) {
//    this.loginDelayFactor = loginDelayFactor;
//  }
//
//  public int getLoginDelayMaxTime() {
//    return this.loginDelayMaxTime;
//  }
//
//  public void setLoginDelayMaxTime(int loginDelayMaxTime) {
//    this.loginDelayMaxTime = loginDelayMaxTime;
//  }
//
//  public int getLoginDelayBase() {
//    return this.loginDelayBase;
//  }
//
//  public void setLoginDelayBase(int loginInitialDelay) {
//    this.loginDelayBase = loginInitialDelay;
//  }
//
//  public List<String> getAdminGroups() {
//    return this.adminGroups;
//  }
//
//  public void setAdminGroups(List<String> adminGroups) {
//    this.adminGroups = adminGroups;
//  }
//
//  public List<String> getAdminUsers() {
//    return this.adminUsers;
//  }
//
//  public void setAdminUsers(List<String> adminUsers) {
//    this.adminUsers = adminUsers;
//  }
//}