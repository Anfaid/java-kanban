import model.Status;
import service.Manager;


public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        manager.createOrUpdateCommonTask(null, "Закончить курс Java", "За 8 месяцев",
                Status.IN_PROGRESS);
        manager.createOrUpdateCommonTask(null, "Зокончить 3 курс", "К июлю 2023 года",
                Status.IN_PROGRESS);
        manager.createOrUpdateEpicTask(null, "Купить MacBook", "До конца 2023 года",
                Status.NEW);
        manager.createOfUpdateSubTask(null, "Накопить денег с праздников",
                "Откладовть деньги", Status.IN_PROGRESS, 3);
        manager.createOfUpdateSubTask(null, "Визуализировать покупку",
                "Выбрать нужную модель", Status.DONE, 3);
        manager.createOrUpdateEpicTask(null, "Купить машину", "Желательно до конца 2023",
                Status.NEW);
        manager.createOfUpdateSubTask(null, "Откладовать деньги с ЗП",
                "Каждый месяц по 30%", Status.IN_PROGRESS, 4);

        System.out.println(manager.getListOfAllTask());
        System.out.println("############");
        manager.createOrUpdateCommonTask(1, "Изменение 1", "Изменение 1", Status.IN_PROGRESS);
        manager.createOfUpdateSubTask(3, "Изменение 1", "Изменение 1",
                Status.IN_PROGRESS, 3);
        System.out.println(manager.getListOfAllTask());
        System.out.println("############");
        manager.deleteByIdentifier(1);
        manager.deleteByIdentifier(3);
        System.out.println(manager.getListOfAllTask());
        System.out.println("############");
        manager.removeAllTask();
        System.out.println(manager.getListOfAllTask());
        System.out.println("The end!");


    }

}
