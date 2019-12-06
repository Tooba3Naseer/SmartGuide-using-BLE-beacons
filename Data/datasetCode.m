MacAddresses = {'D5:9E:76:EE:30:EE', 'C2:6C:15:0C:C1:C7'}; % contains mac addresses of all beacons
Files=dir('C:\Users\HP 820\Desktop\Data'); % give directory of the folder that contains all csv files
RSSI_values = [];
MacAdd =[];
cellarray_data = cell(1,1);   % declare cell array(where each cell contains any type of data) 
counter=1;
for k=3:(length(Files)-2)      
   FileName = Files(k).name; % read file name
   T = readtable(FileName);  % fetch data from csv file and store in table form
   iter1 = 12;
   while FileName(iter1) ~= '_'
       iter1 = iter1+1;
   end
   v = T.DEVICE_NAME;  % fetch row values of device_name column from table
   m = T.MAC_ADDRESS;  % fetch row values of mac_adddress column from table
   MacAdd = [MacAdd, m(1)];
   for i=1:(length(v)/2)
       RSSI_values = [RSSI_values, v(2*i)]; % get RSSI values, eliminate null values and store in vector
       if i~=(length(v)/2)
           MacAdd = [MacAdd, m(2*i+1)];  % get mac address, eliminate null values and store in vector
       end
   end
   for f=1:length(RSSI_values)
       for j=1:length(MacAddresses)
           if isequal(MacAdd(f),MacAddresses(j)) % compare mac addresses of all scanned devices to mac address of beacons
               cellarray_data(counter,j) = RSSI_values(f); % store in cell array only RSSI values of beacons
           end
       end
   end
   cellarray_data(counter,(length(MacAddresses)+1)) = cellstr(FileName(12:iter1-1)); % add room label value in cell array
   % cellstr convert string into cell
   counter=counter+1;
   RSSI_values = [];
   MacAdd =[];
end
for i=1:length(cellarray_data)
    for j=1:length(MacAddresses)
        if isempty(cellarray_data{i,j}) % check if any cell is empty
            a = cellfun(@str2double,cellarray_data(i,1:length(MacAddresses)));
            cellarray_data(i,j) = cellstr(int2str(max(a)+5)); % replace empty value with max of RSSI values +5, 
            %as RSSI values are in negative so max value of +ve values is basically min or weak signal strength
        end
    end
end
for i=1:length(cellarray_data)
     a = cellfun(@str2double,cellarray_data(i,1:length(MacAddresses))); % convert cell array to vector
     cellarray_data(i,1:length(MacAddresses)) = num2cell(a*-1); % negate all RSSI values,convert vector to cell array 
end
T = cell2table(cellarray_data(1:end,:),'VariableNames', {'MacAddr1','MacAddr2', 'RoomLabel'}); % convert cell array into table
writetable(T,'dataset.csv') % write into csv file


