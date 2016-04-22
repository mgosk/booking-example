package init

import java.util.{ArrayList, List}
import javax.inject.Inject

import auth.model._
import auth.repositories.UsersRepository
import it.innove.play.pdf.PdfGenerator
import org.joda.time.DateTime
import play.api.Logger
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.{ArrayList => JavaArrayList, List => JavaList}

class Initializer @Inject()(usersRepository: UsersRepository, pdfGenerator: PdfGenerator) {
  val fonts: JavaList[String] = new JavaArrayList()
  fonts.add("fonts/FreeSans.ttf")
  fonts.add("fonts/Tahoma.ttf")
  pdfGenerator.loadLocalFonts(fonts)

  //admin init
  usersRepository.findByEmail("marcin.gosk+admin@gmail.com").map {
    case Some(user) => //do nothing
    case None =>
      val admin = UserEntity(_id = UserId.random,
        email = Some("marcin.gosk+admin@gmail.com"),
        password = Some("$2a$10$QvGRpkGBKnXIBQmEhs32f.8O1UwP0W4HVgxkJDNBafmaVh3s1oBq."),
        state = UserState.Active,
        isAdmin = true,
        createdAt = DateTime.now,
        hasOrganizationData = false,
        acceptTerms = true,
        newsletter = false,
        companies = Seq(CompanyId.random))
      usersRepository.insert(admin).map { saved =>
        Logger.info("Admin account successfully created")
      }
  }

  //admin init
  usersRepository.getAll.map { seq =>
    seq.map(user =>
      if (user._id.id == user.companies.head.id){
        val oldId = user._id
        val updatedId = UserId.random
        Logger.info(s"updating user ${user.email} oldID:${user._id} newID:${updatedId} ")
        val updatedUser = user.copy(_id = updatedId)
        usersRepository.insert(updatedUser).map{ r=>
           usersRepository.remove(oldId)
        }
      }
    )
  }

}
