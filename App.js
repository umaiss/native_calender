import React, { useEffect } from 'react';
import { Button, View, PermissionsAndroid, NativeModules } from 'react-native';
import CalendarModule, { addEvent } from './src/CalendarModule';
// const CalendarModule = NativeModules.CalendarModule;


const App = () => {

  const requestCalendarPermissions = async () => {
    const granted = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.WRITE_CALENDAR, // Request WRITE_CALENDAR permission
      {
        title: 'Calendar Permission',
        message: 'MyCalendarApp needs access to your calendar to add events.',
      }
    );
    if (granted === PermissionsAndroid.RESULTS.GRANTED) {
      console.log('Calendar permission granted');
    } else {
      console.warn('Calendar permission denied');
    }
  };

  useEffect(() => {
    requestCalendarPermissions()
  }, [])

  const handleAddEvent = () => {
    CalendarModule.addEvent('Sample Event', 'Sample Location', '2024-04-27T10:00:00.000Z', '2024-04-27T12:00:00.000Z')
      .then(eventUri => {
        console.log('Event added:', eventUri);
      })
      .catch(error => {
        console.error('Error adding event:', error);
      });
  };

  return (
    <View>
      <Button title="Add Event" onPress={handleAddEvent} />
    </View>
  );

};

export default App;