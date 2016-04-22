package auth.services

import javax.inject.Inject

import auth.managers.CompanyDataManager
import auth.model.{CompanyData, Identity}

import scala.concurrent.Future

class CompanyDataService @Inject()(organizationDataManager: CompanyDataManager) {

  def upsert(organizationData: CompanyData): Future[CompanyData] =
    organizationDataManager.upsert(organizationData)

  def find()(implicit identity: Identity): Future[Option[CompanyData]] =
    organizationDataManager.find()

}
