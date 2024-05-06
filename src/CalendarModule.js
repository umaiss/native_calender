import { NativeModules } from 'react-native';

const { CalendarModule } = NativeModules;

export default {
  addEvent(title, location, startDate, endDate) {
    return CalendarModule.addEvent(title, location, startDate, endDate);
  }
};
