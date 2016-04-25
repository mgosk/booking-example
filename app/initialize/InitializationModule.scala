package initialize

import com.google.inject.AbstractModule

class InitializationModule  extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[InitializationService]).asEagerSingleton
  }
}
