package auth.managers

import javax.inject.Inject

import auth.model.{CompanyData, Identity}
import auth.repositories.CompanyDataRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CompanyDataManager @Inject()(organizationDataRepository: CompanyDataRepository) {

  def upsert(organizationData: CompanyData): Future[CompanyData] =
    organizationDataRepository.upsert(organizationData).map { i => i }

  def find()(implicit identity: Identity): Future[Option[CompanyData]] =
    organizationDataRepository.find(identity.company.id).map { i => i }

}
